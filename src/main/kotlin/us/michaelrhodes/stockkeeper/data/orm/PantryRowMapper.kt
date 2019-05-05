package us.michaelrhodes.stockkeeper.data.orm

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import us.michaelrhodes.stockkeeper.entity.Pantry
import java.sql.ResultSet
import java.util.*
import kotlin.Comparator

@Component
class PantryRowMapper : RowMapper<Pantry> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Pantry? {
        val uuid = rs.getString("uuid")
        val familyUuid = rs.getString("family_uuid")
        
        return Pantry(UUID.fromString(uuid), UUID.fromString(familyUuid))
    }
}