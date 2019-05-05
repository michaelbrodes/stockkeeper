package us.michaelrhodes.stockkeeper.api.family

import us.michaelrhodes.stockkeeper.entity.Account
import us.michaelrhodes.stockkeeper.entity.FamilyMember
import us.michaelrhodes.stockkeeper.entity.FamilyPermission

@Suppress("unused")
class FamilyMemberInfo(val accountUserName: String, val permission: FamilyPermission) {
    companion object {
        fun create(familyMember: FamilyMember, account: Account) = FamilyMemberInfo(account.username, familyMember.permission)
    }
}