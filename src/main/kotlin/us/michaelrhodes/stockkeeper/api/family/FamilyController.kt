package us.michaelrhodes.stockkeeper.api.family

import org.springframework.http.CacheControl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import us.michaelrhodes.stockkeeper.api.error.APIException
import us.michaelrhodes.stockkeeper.api.error.CommonErrorMessage
import us.michaelrhodes.stockkeeper.api.error.SeverityLevel
import us.michaelrhodes.stockkeeper.data.dao.AccountDAO
import us.michaelrhodes.stockkeeper.data.dao.FamilyDAO
import us.michaelrhodes.stockkeeper.data.dao.FamilyMemberDAO
import us.michaelrhodes.stockkeeper.data.dao.PantryDAO
import us.michaelrhodes.stockkeeper.entity.Family
import us.michaelrhodes.stockkeeper.entity.FamilyMember
import us.michaelrhodes.stockkeeper.entity.FamilyPermission
import us.michaelrhodes.stockkeeper.entity.Pantry
import java.net.URI
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A transactional REST controller that exposes CRUD operations for `family` rows.
 */
@RestController
@Transactional
@RequestMapping("/v1/family")
class FamilyController(private val accountDao: AccountDAO,
                       private val familyDao: FamilyDAO,
                       private val familyMemberDao: FamilyMemberDAO,
                       private val pantryDao: PantryDAO) {

    /**
     * Gets all `family` entities that the supplied user belongs to.
     *
     * @param accountUuid the UUID of a user's account.
     * @return all the families that the user is a `family_member` of.
     */
    @GetMapping
    fun getByAccountUuid(@RequestParam("accountUuid") accountUuid: UUID): ResponseEntity<List<FamilyPanel>> {
        val families = familyDao.getByAccountUuid(accountUuid)
                .map { family ->
                    val familyMembers = familyMemberDao.getByFamilyUuid(family.uuid)
                            .map { familyMember ->
                                val account = accountDao.getByFamilyMemberUuid(familyMember.uuid)
                                        ?: throw APIException.unexpectedError(userMessage = "Error processing members of one of your families. Please contact the developer!",
                                                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                                                severityLevel = SeverityLevel.HIGH,
                                                logMessage = "Cannot map familyMemberUuid=${familyMember.uuid} to an account.")

                                FamilyMemberInfo.create(familyMember, account)
                            }
                    val pantry = pantryDao.getByFamilyUuid(family.uuid)
                            ?: throw APIException.unexpectedError(userMessage = "Cannot find a pantry for your family. Please contact the developer!",
                                    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                                    severityLevel = SeverityLevel.HIGH,
                                    logMessage = "Cannot find a pantry for familyUuid=${family.uuid}")

                    FamilyPanel(family, pantry, familyMembers)
                }

        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES).mustRevalidate())
                .body(families)
    }

    /**
     * Creates a `family`, `pantry`, and owning `family_member` for a user.
     *
     * The user who makes this request will automatically get the [FamilyPermission.OWNER]
     * permission for the family.
     *
     * @param accountUuid the UUID of a user's account.
     * @return the newly created family.
     */
    @PostMapping
    fun createFamily(@RequestParam("accountUuid") accountUuid: UUID,
                     @RequestBody familyRequest: FamilyCreationRequest): ResponseEntity<FamilyPanel> {
        val newFamily = Family(uuid = UUID.randomUUID(),
                name = familyRequest.name,
                createdAt = OffsetDateTime.now(ZoneOffset.UTC),
                updatedAt = OffsetDateTime.now(ZoneOffset.UTC))
        val insertedFamily = familyDao.insert(newFamily)

        val account = accountDao.getByUuid(accountUuid)
                ?: throw APIException.commonError(commonErrorMessage = CommonErrorMessage.NO_ACCOUNT,
                        httpStatus = HttpStatus.NOT_FOUND,
                        severityLevel = SeverityLevel.LOW,
                        logMessage = "Cannot find account entry for accountUuid=$accountUuid")
        val newFamilyMember = FamilyMember(uuid = UUID.randomUUID(),
                accountUuid = accountUuid,
                familyUuid = insertedFamily.uuid,
                permission = FamilyPermission.OWNER)
        val insertedFamilyMember = familyMemberDao.insert(newFamilyMember)

        val newPantry = Pantry(uuid = UUID.randomUUID(), familyUuid = insertedFamily.uuid)
        val insertedPantry = pantryDao.insert(newPantry)

        return ResponseEntity.created(URI.create("/v1/family/${insertedFamily.uuid}"))
                .lastModified(insertedFamily.updatedAt.toEpochSecond())
                .body(FamilyPanel(newFamily,
                        insertedPantry,
                        listOf(FamilyMemberInfo.create(insertedFamilyMember, account))))
    }

    @GetMapping("/{familyUuid}")
    fun getFamily(@PathVariable familyUuid: UUID, @RequestParam accountUuid: UUID): ResponseEntity<Family> {
        val family = familyDao.getByUuid(familyUuid)
                ?: return ResponseEntity.notFound().build()
        val familyMember = familyMemberDao.getByAccountUuidAndFamilyUuid(accountUuid, family.uuid)
                ?: return ResponseEntity.notFound().build()

        if (familyMember.permission in setOf(FamilyPermission.OWNER,
                        FamilyPermission.EDITOR,
                        FamilyPermission.VIEWER)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .lastModified(family.updatedAt.toInstant().toEpochMilli())
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                    .body(family)
        } else {
            throw APIException.commonError(commonErrorMessage = CommonErrorMessage.BAD_FAMILY_PERMISSION,
                    httpStatus = HttpStatus.UNAUTHORIZED,
                    severityLevel = SeverityLevel.MEDIUM,
                    logMessage = "familyMember=${familyMember.uuid} for accountUuid=$accountUuid has permission=${familyMember.permission}, required: OWNER, VIEWER, EDITOR.")
        }

    }

    /**
     * _Marks_ a family entity for deletion.
     */
    @DeleteMapping("/{familyUuid}")
    fun deleteFamily(@PathVariable("familyUuid") familyUuid: UUID,
                     @RequestParam("accountUuid") accountUuid: UUID): ResponseEntity<Void> {
        val familyMember: FamilyMember = familyMemberDao.getByAccountUuidAndFamilyUuid(accountUuid, familyUuid)
                ?: throw APIException.commonError(commonErrorMessage = CommonErrorMessage.NO_FAMILY_MEMBER,
                        httpStatus = HttpStatus.NOT_FOUND,
                        severityLevel = SeverityLevel.LOW, // potentially common since someone might be logged in while someone else disowns them
                        logMessage = "No corresponding family member for familyUuid=$familyUuid and accountUuid=$accountUuid")

        return if (familyMember.permission == FamilyPermission.OWNER) {
            familyDao.markForDeletion(familyUuid)
            ResponseEntity.accepted().build()
        } else {
            throw APIException.commonError(commonErrorMessage = CommonErrorMessage.BAD_FAMILY_PERMISSION,
                    httpStatus = HttpStatus.UNAUTHORIZED,
                    severityLevel = SeverityLevel.MEDIUM,
                    logMessage = "familyMember=${familyMember.uuid} for accountUuid=$accountUuid has permission=${familyMember.permission}, required: OWNER.")
        }
    }

    /**
     *
     */
    @PostMapping("/deleted/{familyUuid}")
    fun restoreFamily(@PathVariable familyUuid: UUID, 
                      @RequestParam("accountUuid") accountUuid: UUID): Family? {
        val familyMember: FamilyMember = familyMemberDao.getByAccountUuidAndFamilyUuid(accountUuid, familyUuid)
                ?: throw APIException.commonError(commonErrorMessage = CommonErrorMessage.NO_FAMILY_MEMBER,
                        httpStatus = HttpStatus.NOT_FOUND,
                        severityLevel = SeverityLevel.LOW, // potentially common since someone might be logged in while someone else disowns them
                        logMessage = "No corresponding family member for familyUuid=$familyUuid and accountUuid=$accountUuid")
        
        return if (familyMember.permission == FamilyPermission.OWNER) {
            familyDao.restore(familyUuid)
        } else {
            throw APIException.commonError(commonErrorMessage = CommonErrorMessage.BAD_FAMILY_PERMISSION,
                    httpStatus = HttpStatus.UNAUTHORIZED,
                    severityLevel = SeverityLevel.MEDIUM,
                    logMessage = "familyMember=${familyMember.uuid} for accountUuid=$accountUuid has permission=${familyMember.permission}, required: OWNER.")
        }
    }

    @PutMapping("/{familyUuid}")
    fun updateFamily(@PathVariable("familyUuid") familyUuid: UUID,
                     @RequestParam("accountUuid") accountUuid: UUID,
                     @RequestBody family: Family): Family {
        val familyMember: FamilyMember = familyMemberDao.getByAccountUuidAndFamilyUuid(accountUuid, familyUuid)
                ?: throw APIException.commonError(commonErrorMessage = CommonErrorMessage.NO_FAMILY_MEMBER,
                        httpStatus = HttpStatus.NOT_FOUND,
                        severityLevel = SeverityLevel.LOW, // potentially common since someone might be logged in while someone else disowns them
                        logMessage = "No corresponding family member for familyUuid=$familyUuid and accountUuid=$accountUuid")

        return if (familyMember.permission in setOf(FamilyPermission.EDITOR, FamilyPermission.OWNER)) {
            val updatedFamily = Family(uuid = family.uuid,
                    name = family.name,
                    createdAt = family.createdAt,
                    updatedAt = OffsetDateTime.now(ZoneOffset.UTC),
                    deletedAt = family.deletedAt)
            familyDao.update(updatedFamily)
        } else {
            throw APIException.commonError(commonErrorMessage = CommonErrorMessage.BAD_FAMILY_PERMISSION,
                    httpStatus = HttpStatus.UNAUTHORIZED,
                    severityLevel = SeverityLevel.MEDIUM,
                    logMessage = "familyMember=${familyMember.uuid} for accountUuid=$accountUuid has permission=${familyMember.permission}, required: OWNER or EDITOR.")
        }
    }
}