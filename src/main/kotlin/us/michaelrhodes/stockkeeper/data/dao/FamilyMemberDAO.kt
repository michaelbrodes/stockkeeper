package us.michaelrhodes.stockkeeper.data.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import us.michaelrhodes.stockkeeper.data.orm.FamilyMemberRowMapper
import us.michaelrhodes.stockkeeper.entity.FamilyMember
import java.util.*

@Repository
class FamilyMemberDAO(private val jdbcTemplate: NamedParameterJdbcTemplate, 
                      private val familyMemberRowMapper: FamilyMemberRowMapper){
    fun insert(familyMember: FamilyMember): FamilyMember {
        val query = """
            INSERT INTO family_member (uuid, account_uuid, family_uuid, family_permission_uid)
            VALUES (:uuid, :accountUuid, :familyUuid, :familyPermissionUid)
        """
        
        val parameters = MapSqlParameterSource()
                .addValue("uuid", familyMember.uuid)
                .addValue("accountUuid", familyMember.accountUuid)
                .addValue("familyUuid", familyMember.familyUuid)
                .addValue("familyPermissionUid", familyMember.permission.uid)
        
        jdbcTemplate.update(query, parameters)
        
        return familyMember
    }

    fun getByAccountUuidAndFamilyUuid(accountUuid: UUID, familyUuid: UUID): FamilyMember? {
        val query = """
            SELECT 
              fm.uuid, 
              fm.account_uuid, 
              fm.family_uuid, 
              fm.family_permission_uid
            FROM family_member fm
            WHERE fm.family_uuid = :familyUuid AND fm.account_uuid = :accountUuid
        """
        
        val parameters = MapSqlParameterSource() 
                .addValue("familyUuid", familyUuid)
                .addValue("accountUuid", accountUuid)
        
        return jdbcTemplate.queryForObject(query, parameters, familyMemberRowMapper)
    }

    fun getByFamilyUuid(uuid: UUID): List<FamilyMember> {
        val query = """
            SELECT 
              fm.uuid, 
              fm.account_uuid, 
              fm.family_uuid, 
              fm.family_permission_uid
            FROM family_member fm
            WHERE fm.family_uuid = :familyUuid
        """
        
        val parameters = MapSqlParameterSource()
                .addValue("familyUuid", uuid)
        
        return jdbcTemplate.query(query, parameters, familyMemberRowMapper)
    }

    fun getByUuid(uuid: UUID): FamilyMember? {
        val query = """
            SELECT 
              fm.uuid, 
              fm.account_uuid, 
              fm.family_uuid, 
              fm.family_permission_uid
            FROM family_member fm
            WHERE fm.uuid = :uuid
        """

        val parameters = MapSqlParameterSource()
                .addValue("uuid", uuid)

        return jdbcTemplate.queryForObject(query, parameters, familyMemberRowMapper)
    }
}
