package com.motycka.edu.tracker.user

import com.motycka.edu.tracker.user.model.NewUser
import com.motycka.edu.tracker.user.model.User
import org.springframework.stereotype.Repository

@Repository
interface UserRepository {
    fun selectAll(): List<User>
    fun selectById(id: Long): User?
    fun selectByName(name: String): User?
    fun insert(user: NewUser): User
    fun update(user: User): User
    fun delete(id: Long)
}
