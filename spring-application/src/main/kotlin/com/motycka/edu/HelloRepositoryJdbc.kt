package com.motycka.edu

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

data class Message(
    val locale: String,
    val messageKey: String,
    val messageValue: String
)

@Repository
class HelloRepositoryJdbc(
    private val jdbcTemplate: JdbcTemplate
) {

    fun selectMessage(locale: String, key: String): Message? {
        return jdbcTemplate.query(
            "SELECT * FROM greeting WHERE locale = ? AND message_key = ? LIMIT 1",
            ::rowMapper,
            locale,
            key
        ).firstOrNull()
    }

    fun insertMessage(message: Message) {
        jdbcTemplate.update(
            "INSERT INTO greeting (locale, message_key, message_value) VALUES (?, ?, ?)",
            message.locale,
            message.messageKey,
            message.messageValue
        )
    }

    private fun rowMapper(rs: ResultSet, i: Int): Message {
        return Message(
            locale = rs.getString("locale"),
            messageKey = rs.getString("message_key"),
            messageValue = rs.getString("message_value")
        )
    }

    companion object {
        const val HELLO_MESSAGE_KEY = "hello"
    }
}
