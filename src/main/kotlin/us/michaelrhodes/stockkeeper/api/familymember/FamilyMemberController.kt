package us.michaelrhodes.stockkeeper.api.familymember

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import us.michaelrhodes.stockkeeper.data.dao.FamilyMemberDAO
import us.michaelrhodes.stockkeeper.entity.FamilyMember
import java.util.UUID

@RestController
@RequestMapping("/v1/family-member")
class FamilyMemberController(private val familyMemberDAO: FamilyMemberDAO) {
    @GetMapping("/{familyMemberUuid}")
    fun getFamilyMember(@PathVariable familyMemberUuid : UUID) : FamilyMember? {
        return familyMemberDAO.getByUuid(familyMemberUuid)
    }
}