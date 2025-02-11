package com.motycka.edu.tracker.exercise

import com.motycka.edu.tracker.exercise.model.Exercise
import com.motycka.edu.tracker.exercise.model.NewExercise
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import java.time.ZoneOffset
import kotlin.jvm.Throws

@Repository
class ExerciseRepository(
    private val jdbcTemplate: JdbcTemplate
) {

    fun selectAll(userId: Long): List<Exercise> {
        return jdbcTemplate.query("SELECT * FROM exercise WHERE user_id = ?", this::rowMapper, userId)
    }

    fun selectById(userId: Long, id: Long): Exercise {
        return jdbcTemplate.query(
            "SELECT * FROM exercise WHERE id = ? AND user_id = ? LIMIT 1",
            this::rowMapper,
            id,
            userId
        ).stream().findFirst().orElse(null)
    }

    fun insert(exercise: NewExercise): Exercise {
        return jdbcTemplate.query(
            "SELECT * FROM FINAL TABLE (INSERT INTO exercise (user_id, activity_id, start_time, duration) VALUES (?, ?, ?, ?))",
            this::rowMapper,
            exercise.userId,
            exercise.activityId,
            Timestamp.from(exercise.startTime.withZoneSameInstant(ZoneOffset.UTC).toInstant()),
            exercise.duration
        ).stream().findFirst().orElse(null)

    }

    fun update(userId: Long, exercise: Exercise): Exercise {
        return jdbcTemplate.query(
            "SELECT * FROM FINAL TABLE (UPDATE exercise SET start_time = ?, duration = ? WHERE id = ? AND user_id = ?)",
            this::rowMapper,
            Timestamp.from(exercise.startTime.withZoneSameInstant(ZoneOffset.UTC).toInstant()),
            exercise.duration,
            exercise.id,
            userId
        ).stream().findFirst().orElse(null)
    }

    fun delete(userId: Long, id: Long) {
        jdbcTemplate.update("DELETE FROM exercise WHERE id = ? AND user_id = ?", id, userId)
    }

    @Throws(SQLException::class)
    private fun rowMapper(rs: ResultSet, i: Int): Exercise{
        return Exercise(
            rs.getLong("id"),
            rs.getLong("user_id"),
            rs.getLong("activity_id"),
            rs.getTimestamp("start_time").toInstant().atZone(ZoneOffset.UTC),
            rs.getDouble("duration"),
            null
        )
    }

}
