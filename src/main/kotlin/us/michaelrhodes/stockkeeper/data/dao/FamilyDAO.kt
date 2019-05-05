package us.michaelrhodes.stockkeeper.data.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import us.michaelrhodes.stockkeeper.data.orm.FamilyRowMapper
import us.michaelrhodes.stockkeeper.entity.Family
import java.util.*

@Repository
class FamilyDAO(private val jdbcTemplate: NamedParameterJdbcTemplate,
                private val familyRowMapper: FamilyRowMapper) {
    fun getByAccountUuid(accountUuid: UUID): List<Family> {
        val query = """
            SELECT 
              f.uuid, 
              f.name, 
              f.created_at AT TIME ZONE 'UTC' as created_at, 
              f.updated_at AT TIME ZONE 'UTC' as updated_at, 
              f.deleted_at AT TIME ZONE 'UTC' as deleted_At
            FROM family f
            JOIN family_member fm
              ON f.uuid = fm.family_uuid
            WHERE fm.account_uuid = :accountUuid
            ORDER BY f.updated_at
        """

        val parameter = MapSqlParameterSource()
                .addValue("accountUuid", accountUuid)

        return jdbcTemplate.query(query, parameter, familyRowMapper)
    }

    fun insert(family: Family): Family {
        val query = """
            INSERT INTO family(
              uuid, 
              name, 
              created_at, 
              updated_at, 
              deleted_at)
            VALUES (
              :uuid, 
              :name, 
              :createdAt, 
              :updatedAt, 
              :deletedAt)
        """

        val parameters = MapSqlParameterSource()
                .addValue("uuid", family.uuid)
                .addValue("name", family.name)
                .addValue("createdAt", family.createdAt)
                .addValue("updatedAt", family.updatedAt)
                .addValue("deletedAt", family.deletedAt)

        jdbcTemplate.update(query, parameters)

        return family
    }

    fun markForDeletion(familyUuid: UUID) {
        val query = """
            UPDATE family 
            SET deleted_at = NOW() 
            WHERE uuid = :familyUuid
        """

        val parameter = MapSqlParameterSource()
                .addValue("familyUuid", familyUuid)

        jdbcTemplate.update(query, parameter)
    }

    fun update(family: Family): Family {
        val query = """
            UPDATE family 
            SET name = :name, updated_at = :updatedAt 
            WHERE uuid = :familyUuid
        """

        val parameters = MapSqlParameterSource()
                .addValue("name", family.name)
                .addValue("updatedAt", family.updatedAt)
                .addValue("familyUuid", family.uuid)

        jdbcTemplate.update(query, parameters)

        return family
    }

    fun getByUuid(familyUuid: UUID): Family? {
        val query = """
            SELECT 
              f.uuid, 
              f.name, 
              f.created_at AT TIME ZONE 'UTC' as created_at, 
              f.updated_at AT TIME ZONE 'UTC' as updated_at, 
              f.deleted_at AT TIME ZONE 'UTC' as deleted_at
            FROM family f
            WHERE f.uuid = :familyUuid
        """

        val parameter = MapSqlParameterSource()
                .addValue("familyUuid", familyUuid)

        return jdbcTemplate.queryForFirst(query, parameter, familyRowMapper)
    }

    fun restore(familyUuid: UUID): Family? {
        val query = """
            UPDATE family 
            SET deleted_at = NULL
            WHERE uuid = :familyUuid
        """

        val parameter = MapSqlParameterSource()
                .addValue("familyUuid", familyUuid)

        jdbcTemplate.update(query, parameter)
        
        return getByUuid(familyUuid)
    }
}
