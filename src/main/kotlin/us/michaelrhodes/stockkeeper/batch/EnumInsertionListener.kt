package us.michaelrhodes.stockkeeper.batch

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import us.michaelrhodes.stockkeeper.entity.FamilyPermission

@Component
class EnumInsertionListener(val jdbcTemplate: NamedParameterJdbcTemplate) 
    : ApplicationListener<ContextRefreshedEvent> {

    companion object {
        val LOG : Logger = LoggerFactory.getLogger(EnumInsertionListener::class.java)
    }
    
    @Transactional
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        insertFamilyPermissions()
    }

    private fun insertFamilyPermissions() {
        LOG.debug("Beginning insertion of FamilyPermission enum constants!")
        val parameters  = FamilyPermission.values()
                .map {
                    MapSqlParameterSource()
                            .addValue("uid", it.uid)
                            .addValue("permission", it.name)
                }
                .toTypedArray()

        val updatedRecords = jdbcTemplate.batchUpdate(
                """INSERT INTO family_permission(uid, permission)
                    |VALUES (:uid, :permission)
                    |ON CONFLICT DO NOTHING""".trimMargin(),
                parameters)
                .sum()
        LOG.info("Inserted FamilyPermission constants: {}", updatedRecords)
    }
}