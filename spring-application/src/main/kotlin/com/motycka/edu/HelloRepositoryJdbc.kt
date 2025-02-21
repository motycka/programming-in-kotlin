package com.motycka.edu

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class HelloRepositoryJdbc(
    private val jdbcTemplate: JdbcTemplate
) {

    fun selectHello(locale: String): Hello? {
        return jdbcTemplate.query(
            "SELECT * FROM hello WHERE locale = ? LIMIT 1",
            ::rowMapper,
            locale
        ).firstOrNull()
    }

    fun insertHello(greeting: Hello) {
        jdbcTemplate.update(
            "INSERT INTO hello (locale, hello) VALUES (?, ?)",
            greeting.locale,
            greeting.hello,
        )
    }

    private fun rowMapper(rs: ResultSet, i: Int): Hello {
        return Hello(
            locale = rs.getString("locale"),
            hello = rs.getString("hello")
        )
    }
}
