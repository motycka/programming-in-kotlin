package com.motycka.edu

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class HelloRepositoryJdbc(
    private val jdbcTemplate: JdbcTemplate
) {

    fun selectMessage(locale: String, key: String): Greeting? {
        return jdbcTemplate.query(
            "SELECT * FROM greeting WHERE locale = ? AND message_key = ? LIMIT 1",
            ::rowMapper,
            locale,
            key
        ).firstOrNull()
    }

    fun insertMessage(greeting: Greeting) {
        jdbcTemplate.update(
            "INSERT INTO greeting (locale, message_key, message_value) VALUES (?, ?, ?)",
            greeting.locale,
            greeting.messageKey,
            greeting.messageValue
        )
    }

    private fun rowMapper(rs: ResultSet, i: Int): Greeting {
        return Greeting(
            locale = rs.getString("locale"),
            messageKey = rs.getString("message_key"),
            messageValue = rs.getString("message_value")
        )
    }

    companion object {
        const val HELLO_MESSAGE_KEY = "hello"
    }
}
