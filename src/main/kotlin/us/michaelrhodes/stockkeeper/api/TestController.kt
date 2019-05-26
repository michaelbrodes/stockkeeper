package us.michaelrhodes.stockkeeper.api

import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import us.michaelrhodes.stockkeeper.PROD
import us.michaelrhodes.stockkeeper.data.dao.EMPTY

@RestController("!$PROD")
@RequestMapping("test")
class TestController(val jdbcTemplate: NamedParameterJdbcTemplate) {
    /**
     * This will clear all data from all tables. This is only useful for integration tests and will
     * never be exposed in production.
     */
    @Suppress("SqlWithoutWhere")
    @DeleteMapping(value = ["/all-data"], produces = [MediaType.TEXT_PLAIN_VALUE])
    @Transactional
    fun deleteAllDataSuperDangerous(): String {
        jdbcTemplate.update("""
            -- done in an order that preserves foreign keys.
            TRUNCATE in_stock_pantry_item, 
                     pantry_item, 
                     pantry_item_category, 
                     product, 
                     family_member,
                     account, 
                     pantry, 
                     family
                     CASCADE
            """, EMPTY)

        return "Data has been purged."
    }
}