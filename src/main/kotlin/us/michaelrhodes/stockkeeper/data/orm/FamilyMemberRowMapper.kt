package us.michaelrhodes.stockkeeper.data.orm

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import us.michaelrhodes.stockkeeper.entity.FamilyMember
import us.michaelrhodes.stockkeeper.entity.FamilyPermission
import java.sql.ResultSet
import java.util.*

@Component
class FamilyMemberRowMapper : RowMapper<FamilyMember> {
    override fun mapRow(rs: ResultSet, rowNum: Int): FamilyMember? {
        val uuid = rs.getString("uuid")!!
        val accountUuid = rs.getString("account_uuid")!!
        val familyUuid = rs.getString("family_uuid")!!
        val familyPermissionUid = rs.getLong("family_permission_uid")
        
        return FamilyMember(UUID.fromString(uuid), 
                UUID.fromString(accountUuid), 
                UUID.fromString(familyUuid), 
                FamilyPermission.fromUid(familyPermissionUid))
    }
}