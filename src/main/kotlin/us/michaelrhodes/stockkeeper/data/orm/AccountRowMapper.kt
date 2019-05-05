package us.michaelrhodes.stockkeeper.data.orm

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import us.michaelrhodes.stockkeeper.entity.Account
import java.sql.ResultSet
import java.util.*

@Component
class AccountRowMapper : RowMapper<Account> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Account? {
        val uuid = rs.getString("uuid")
        val username = rs.getString("username")
        
        return Account(UUID.fromString(uuid), username)
    }

}