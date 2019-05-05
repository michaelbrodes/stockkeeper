package us.michaelrhodes.stockkeeper.api.health

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import us.michaelrhodes.stockkeeper.data.dao.queryForFirst

@RestController
@RequestMapping("/health")
class HealthController(val jdbcTemplate: NamedParameterJdbcTemplate) {
    companion object {
        const val BYTES_IN_MB = 1024 * 1024
        val LOG = LoggerFactory.getLogger(HealthController::class.java)
    }

    @GetMapping("/")
    fun getHealth(): Health {
        val stockkeeperHealth = calculateStockkeeperHealth()

        val dbHealth = try {
            calculateDBHealth()
        } catch (e : Exception) {
            LOG.error("Error trying to communicate with backend database.", e)
            DBHealth(ServiceStatus.DOWN)
        }
        
        return Health(stockkeeperHealth, dbHealth)
    }

    private fun calculateStockkeeperHealth(): StockkeeperHealth {
        val runtime = Runtime.getRuntime()

        val freeHeapInMB = runtime.freeMemory() / BYTES_IN_MB
        val totalHeapInMB = runtime.totalMemory() / BYTES_IN_MB
        val maxHeapInMB = runtime.maxMemory() / BYTES_IN_MB
        val heapUsedInMB = totalHeapInMB - freeHeapInMB

        return StockkeeperHealth(heapUsedInMB = heapUsedInMB,
                freeHeapInMB = freeHeapInMB,
                maxHeapInMB = maxHeapInMB,
                totalHeapInMB = totalHeapInMB,
                serviceStatus = ServiceStatus.UP)
    }

    private fun calculateDBHealth(): DBHealth {
        val totalDiskUsageQuery = """
            SELECT pg_size_pretty(SUM(pg_database_size(pg_database.datname))) as disk_usage
            FROM pg_database;
        """.trimIndent()
        val totalDiskUsage = jdbcTemplate.queryForFirst(totalDiskUsageQuery, RowMapper { rs, _ -> 
            rs.getString("disk_usage")
        })!!

        val diskUsageByTableQuery = """
            SELECT pc.relname                                     AS table,
                   pg_size_pretty(pg_total_relation_size(pc.oid)) AS disk_usage
            FROM pg_class pc
            LEFT JOIN pg_namespace pn
              ON (pn.oid = pc.relnamespace)
            WHERE pn.nspname <> 'pg_catalog'
              AND pn.nspname <> 'information_schema'
              AND pc.relkind <> 'i'
              AND pn.nspname !~ '^pg_toast'
            ORDER BY pg_total_relation_size(pc.oid) DESC;
        """.trimIndent()
        
        val tableToDiskUsage = LinkedHashMap<String, String>()
        
        jdbcTemplate.query(diskUsageByTableQuery) { 
            rs -> tableToDiskUsage[rs.getString("table")] = rs.getString("disk_usage")
        }
        
        return DBHealth(serviceStatus = ServiceStatus.UP, 
                totalDiskUsage = totalDiskUsage, 
                tableToDiskUsage = tableToDiskUsage)
    }
}

