package com.motycka.edu.tracker.activity.model

data class NewActivity(
    val userId: Long,
    val type: ActivityType,
    val name: String,
    val kcalPerMinute: Double
) {
    fun toActivity(id: Long): Activity {
        return Activity(id, userId, type, name, kcalPerMinute)
    }
}
