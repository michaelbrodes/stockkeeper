package us.michaelrhodes.stockkeeper.entity

import java.util.*

data class FamilyMember (val uuid: UUID,
                         val accountUuid: UUID, 
                         val familyUuid: UUID, 
                         val permission: FamilyPermission)