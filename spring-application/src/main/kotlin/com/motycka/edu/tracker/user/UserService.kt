package com.motycka.edu.tracker.user

import com.motycka.edu.tracker.user.model.NewUser
import com.motycka.edu.tracker.user.model.User

interface UserService {
    fun getUsers(): List<User>
    fun getUserById(id: Long): User?
    fun getUserByName(name: String): User?
    fun createUser(user: NewUser): User
    fun updateUser(user: User): User
    fun deleteUser(id: Long)
}
