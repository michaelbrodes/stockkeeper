package us.michaelrhodes.stockkeeper.data.dao

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource

fun <T> NamedParameterJdbcTemplate.queryForFirst(query: String,
                                                 parameters: SqlParameterSource,
                                                 rowMapper: RowMapper<T>) : T? {
    return this.query(query, parameters, rowMapper)
            .firstOrNull()
}

fun <T> NamedParameterJdbcTemplate.queryForFirst(query: String, 
                                                 rowMapper: RowMapper<T>) : T? {
    return this.query(query, rowMapper).firstOrNull()
}

