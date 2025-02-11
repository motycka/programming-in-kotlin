package com.motycka.edu.tracker.user.model

data class NewUser(
    val name: String,
    val role: UserRole
) {
    fun toUser(id: Long, role: UserRole): User {
        return User(id = id, name = name, role = role)
    }
}
