package com.motycka.edu.tracker.exercise.model

import java.time.ZonedDateTime

data class NewExercise(
    val userId: Long,
    val activityId: Long,
    val startTime: ZonedDateTime,
    val duration: Double
) {
    fun toExercise(id: Long, kcalBurned: Double): Exercise {
        return Exercise(id, userId, activityId, startTime, duration, kcalBurned)
    }
}
