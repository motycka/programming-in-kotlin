package com.motycka.edu.tracker.activity.model

data class Activity(
    val id: Long,
    val userId: Long,
    val type: ActivityType,
    val name: String,
    val kcalPerMinute: Double
) {
    fun withUserId(userId: Long): Activity {
        return Activity(id, userId, type, name, kcalPerMinute)
    }
}
