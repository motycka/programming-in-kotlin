package com.motycka.edu.game.user

import com.motycka.edu.game.user.model.Account
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.SQLException
import kotlin.collections.firstOrNull

private val logger = KotlinLogging.logger {}

/**
 * This is an example of repository implementation using JdbcTemplate.
 */
@Repository
class AccountRepository(
    private val jdbcTemplate: JdbcTemplate
) : IAccountRepository {

    override fun selectByUsername(username: String): Account? {
        logger.debug { "Selecting user $username" }
        return jdbcTemplate.query(
            "SELECT * FROM account WHERE username = ? LIMIT 1",
            ::rowMapper,
            username
        ).firstOrNull()
    }

    override fun insert(user: Account): Account? {
        logger.debug { "Inserting new user ${user.copy(password = "***")}" }
        return jdbcTemplate.query(
            "SELECT * FROM FINAL TABLE (INSERT INTO account (name, username, password) VALUES (?, ?, ?, ?))",
            ::rowMapper,
            user.name,
            user.username,
            user.password
        ).firstOrNull()
    }

    @Throws(SQLException::class)
    private fun rowMapper(rs: ResultSet, i: Int): Account {
        return Account(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("username"),
            rs.getString("password")
        )
    }
}
