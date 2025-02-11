package com.motycka.edu.tracker.activity

import com.motycka.edu.tracker.activity.model.Activity
import com.motycka.edu.tracker.activity.model.ActivityType
import com.motycka.edu.tracker.activity.model.NewActivity
import com.motycka.edu.tracker.config.ApplicationConfiguration
import mu.KLogging
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

import java.sql.ResultSet
import java.sql.SQLException
import kotlin.jvm.Throws

@Repository
class ActivityRepository(
    private val jdbcTemplate: JdbcTemplate
) {

    fun selectActivities(userId: Long): List<Activity> {
        return jdbcTemplate.query(
            "SELECT * FROM activity WHERE user_id in (?, ?)",
            this::rowMapper,
            userId,
            ApplicationConfiguration.SYSTEM_USER_ID
        )
    }

    fun selectActivityById(userId: Long, id: Long): Activity? {
        return jdbcTemplate.query(
            "SELECT * FROM activity WHERE user_id in (?, ?) AND id = ? LIMIT 1",
            this::rowMapper,
            userId,
            ApplicationConfiguration.SYSTEM_USER_ID,
            id
        ).firstOrNull()
    }

    fun existsByName(userId: Long, name: String): Boolean {
        return jdbcTemplate.query(
            "SELECT * FROM activity WHERE user_id in (?, ?) AND name = ? LIMIT 1",
            this::rowMapper,
            userId,
            ApplicationConfiguration.SYSTEM_USER_ID,
            name
        ).isNotEmpty()
    }

    @Transactional
    fun insertActivity(activity: NewActivity): Activity? {
        logger.info("Inserting activity: " + activity.name + " for user: " + activity.userId)
        return jdbcTemplate.query(
//                returning("INSERT INTO activity (name, user_id, kcal_per_minute) VALUES (?, ?, ?)"),
//                "SELECT id, name, user_id, kcal_per_minute FROM FINAL TABLE (\" + sql + \")",
            "INSERT INTO activity (name, user_id, kcal_per_minute) VALUES (?, ?, ?) RETURNING *",
            this::rowMapper,
            activity.name,
            activity.userId,
            activity.kcalPerMinute
        ).firstOrNull()
    }

    @Transactional
    fun updateActivity(userId: Long, activity: Activity): Activity? {
        logger.info("Updating activity: " + activity.name + " for user: " + activity.userId)
        jdbcTemplate.update(
            "UPDATE activity SET name = ?, kcal_per_minute = ? WHERE user_id in (?, ?) AND id = ?",
            activity.name,
            activity.kcalPerMinute,
            userId,
            ApplicationConfiguration.SYSTEM_USER_ID,
            activity.id
        )
        return selectActivityById(userId, activity.id)
    }

    @Transactional
    fun deleteActivity(userId: Long, id: Long) {
        jdbcTemplate.update(
            "DELETE FROM activity WHERE user_id in (?, ?) AND id = ?",
            userId,
            ApplicationConfiguration.SYSTEM_USER_ID,
            id
        )
    }

    @Throws(SQLException::class)
    private fun rowMapper(rs: ResultSet, i: Int): Activity {
        return Activity(
            rs.getLong("id"),
            rs.getLong("user_id"),
            getActivityType(rs.getLong("user_id")),
            rs.getString("name"),
            rs.getDouble("kcal_per_minute")
        )
    }

    private fun getActivityType(userId: Long): ActivityType {
        return when (userId) {
            ApplicationConfiguration.SYSTEM_USER_ID -> ActivityType.SYSTEM
            else -> ActivityType.USER
        }
    }

    companion object: KLogging()

}
