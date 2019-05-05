package us.michaelrhodes.stockkeeper.data.dao

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import us.michaelrhodes.stockkeeper.entity.Pantry
import java.util.*

@Repository
class PantryDAO(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    fun insert(pantry: Pantry): Pantry {
        TODO("not implemented")
    }

    fun getByFamilyUuid(uuid: UUID): Pantry? {
        TODO("not implemented")
    }
}
