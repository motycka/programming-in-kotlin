package com.motycka.edu.tracker.user.model

data class User(
    val id: Long,
    val name: String,
    val role: UserRole
) {
    fun copyWithId(id: Long): User {
        return User(id = id, name = name, role = role);
    }
}
