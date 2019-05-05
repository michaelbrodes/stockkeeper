package us.michaelrhodes.stockkeeper.data.orm

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import us.michaelrhodes.stockkeeper.entity.Family
import java.sql.ResultSet
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Component
class FamilyRowMapper : RowMapper<Family> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Family? {
        val uuid  = rs.getString("uuid")!!
        val name = rs.getString("name")!!
        val createdAt = rs.getTimestamp("created_at")!!
        val updatedAt = rs.getTimestamp("updated_at")!!
        val deletedAt = rs.getTimestamp("deleted_at")
        
        val deletedAtWithTimeZone = if (deletedAt != null) {
            OffsetDateTime.ofInstant(deletedAt.toInstant(), ZoneOffset.UTC)
        } else {
            null
        }
        
        return Family(uuid = UUID.fromString(uuid), 
                name = name, 
                createdAt = OffsetDateTime.ofInstant(createdAt.toInstant(), ZoneOffset.UTC),
                updatedAt = OffsetDateTime.ofInstant(updatedAt.toInstant(), ZoneOffset.UTC), 
                deletedAt = deletedAtWithTimeZone)
    }

}