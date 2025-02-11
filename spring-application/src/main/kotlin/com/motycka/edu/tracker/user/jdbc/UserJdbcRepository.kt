package com.motycka.edu.tracker.user.jdbc

import com.motycka.edu.tracker.user.UserRepository
import com.motycka.edu.tracker.user.model.NewUser
import com.motycka.edu.tracker.user.model.User
import com.motycka.edu.tracker.user.model.UserRole
import mu.KLogging
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.SQLException

/**
 * This is an example of repository implementation using JdbcTemplate.
 */
@Repository
class UserJdbcRepository(
    private val jdbcTemplate: JdbcTemplate
) : UserRepository {

    override fun selectAll(): List<User> {
        logger.debug("Selecting all users")
        return jdbcTemplate.query(
            "SELECT * FROM users"
        ) { resultSet, index ->
            User(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                UserRole.valueOf(resultSet.getString("role"))
            )
        }
    }

    override fun selectById(id: Long): User? {
        logger.debug("Selecting user $id")
        return jdbcTemplate.query(
            "SELECT * FROM users WHERE id = ? LIMIT 1",
            this::rowMapper, id)
            .stream()
            .findFirst()
            .orElse(null)
    }

    override fun selectByName(name: String): User? {
        logger.debug("Selecting user $name")
        return jdbcTemplate.query(
            "SELECT * FROM users WHERE name = ? LIMIT 1",
            this::rowMapper, name)
            .stream()
            .findFirst()
            .orElse(null)
    }

    override fun insert(user: NewUser): User {
        logger.debug("Inserting new user: $user")
        return jdbcTemplate.query(
            "SELECT * FROM FINAL TABLE (INSERT INTO users (name, role) VALUES (?, ?))",
            this::rowMapper,
            user.name,
            user.role.name
        ).stream().findFirst().orElse(null)
    }

    override fun update(user: User): User {
        logger.debug("Updating user: $user")
        return jdbcTemplate.query(
            "SELECT * FROM FINAL TABLE (UPDATE users SET name = ? WHERE id = ?)",
            this::rowMapper,
            user.name,
            user.id
        ).stream().findFirst().orElse(null)
    }

    override fun delete(id: Long) {
        logger.debug("Deleting user $id")
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id)
    }

    @Throws(SQLException::class)
    private fun rowMapper(rs: ResultSet, i: Int): User {
        return User(
            rs.getLong("id"),
            rs.getString("name"),
            UserRole.valueOf(rs.getString("role"))
        )
    }

    companion object: KLogging()
}
