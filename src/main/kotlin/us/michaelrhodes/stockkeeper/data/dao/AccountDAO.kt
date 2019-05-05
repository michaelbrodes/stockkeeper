package us.michaelrhodes.stockkeeper.data.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import us.michaelrhodes.stockkeeper.data.orm.AccountRowMapper
import us.michaelrhodes.stockkeeper.entity.Account
import java.util.*

@Repository
class AccountDAO (private val jdbcTemplate: NamedParameterJdbcTemplate, 
                  private val accountRowMapper: AccountRowMapper) {
    fun getByFamilyMemberUuid(uuid: UUID): Account? {
        val query = """
            SELECT 
              a.uuid, 
              a.username
            FROM account a
            JOIN family_member fm 
              ON fm.account_uuid = a.uuid
            WHERE fm.uuid = :familyMemberUuid
        """.trimIndent()
        
        val parameters = MapSqlParameterSource()
                .addValue("familyMemberUuid", uuid)
        
        return jdbcTemplate.queryForFirst(query, parameters, accountRowMapper)
    }

    fun getByUuid(accountUuid: UUID): Account? {
        val query = """
            SELECT 
              a.uuid, 
              a.username
            FROM account a
            WHERE a.uuid = :accountUuid
        """.trimIndent()

        val parameters = MapSqlParameterSource()
                .addValue("accountUuid", accountUuid)

        return jdbcTemplate.queryForFirst(query, parameters, accountRowMapper)
    }

    fun insert(account: Account): Account {
        val query = """
            INSERT INTO account(uuid, username)
            VALUES (:uuid, :username)
        """.trimIndent()
        
        val parameters = MapSqlParameterSource()
                .addValue("uuid", account.uuid)
                .addValue("username", account.username)
        
        jdbcTemplate.update(query, parameters)
        
        return account
    }

}
