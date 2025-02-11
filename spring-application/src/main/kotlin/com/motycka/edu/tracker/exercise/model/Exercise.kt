package com.motycka.edu.tracker.exercise.model

import java.time.ZonedDateTime

data class Exercise(
    val id: Long,
    val userId: Long,
    val activityId: Long,
    val startTime: ZonedDateTime,
    val duration: Double,
    val caloriesBurned: Double?
) {
    fun withId(id: Long): Exercise {
        return Exercise(id, userId, activityId, startTime, duration, caloriesBurned)
    }
}

