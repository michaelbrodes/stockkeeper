package us.michaelrhodes.stockkeeper.data.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import us.michaelrhodes.stockkeeper.data.orm.PantryRowMapper
import us.michaelrhodes.stockkeeper.entity.Pantry
import java.util.*

@Repository
class PantryDAO(private val jdbcTemplate: NamedParameterJdbcTemplate, 
                private val pantryRowMapper: PantryRowMapper) {
    fun insert(pantry: Pantry): Pantry {
        val query = """
            INSERT INTO pantry(uuid, family_uuid)
            VALUES (:uuid, :familyUuid)
        """
        
        val parameters = MapSqlParameterSource()
                .addValue("uuid", pantry.uuid)
                .addValue("familyUuid", pantry.familyUuid)
        
        jdbcTemplate.update(query, parameters)
        
        return pantry
    }

    fun getByFamilyUuid(uuid: UUID): Pantry? {
        val query = """
            SELECT
              uuid,
              family_uuid
            FROM pantry 
            WHERE family_uuid = :familyUuid
        """
        
        val paramter = MapSqlParameterSource()
                .addValue("familyUuid", uuid)
        
        return jdbcTemplate.queryForFirst(query, paramter, pantryRowMapper)
    }
}
