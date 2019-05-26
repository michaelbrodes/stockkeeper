package us.michaelrhodes.stockkeeper.data.dao

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource

/**
 * Executes a query and gets the first result. Unlike [NamedParameterJdbcTemplate.queryForObject]
 * this method doesn't throw an error if more than one result exists.
 *
 * @param query A templated SQL query.
 * @param parameters The named parameters for the templated query.
 * @param rowMapper Maps a SQL result set to a domain object.
 */
fun <T> NamedParameterJdbcTemplate.queryForFirst(query: String,
                                                 parameters: SqlParameterSource,
                                                 rowMapper: RowMapper<T>): T? {
    return this.query(query, parameters, rowMapper)
            .firstOrNull()
}

/**
 * Executes a query and gets the first result. Unlike [NamedParameterJdbcTemplate.queryForObject]
 * this method doesn't throw an error if more than one result exists.
 *
 * @param query A templated SQL query.
 * @param rowMapper Maps a SQL result set to a domain object.
 */
fun <T> NamedParameterJdbcTemplate.queryForFirst(query: String,
                                                 rowMapper: RowMapper<T>): T? {
    return this.query(query, rowMapper).firstOrNull()
}

val EMPTY = EmptySqlParameterSource.INSTANCE

