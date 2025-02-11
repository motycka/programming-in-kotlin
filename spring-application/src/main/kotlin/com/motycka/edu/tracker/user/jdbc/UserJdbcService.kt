package com.motycka.edu.tracker.user.jdbc

import com.motycka.edu.tracker.authorization.AuthorizationService
import com.motycka.edu.tracker.error.ForbiddenException
import com.motycka.edu.tracker.user.UserService
import com.motycka.edu.tracker.user.model.NewUser
import com.motycka.edu.tracker.user.model.User
import mu.KLogging
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

/**
 * The UserJdbcService uses the UserJdbcRepository to perform CRUD operations on users.
 */
@Primary
@Service
class UserJdbcService(
    private val userRepository: UserJdbcRepository,
    private val authorizationService: AuthorizationService
) : UserService {

    override fun getUsers(): List<User> {
        if (authorizationService.isSystem()) {
            logger.debug { "Getting all users" }
            return userRepository.selectAll()
        } else throw forbidden()
    }

    override fun getUserById(id: Long): User? {
        if (authorizationService.isSystem()) {
            logger.debug { "Getting user $id" }
            return userRepository.selectById(id)
        } else throw forbidden()
    }

    override fun getUserByName(name: String): User? {
        if (authorizationService.isSystem()) {
            logger.debug { "Getting user $name" }
            return userRepository.selectByName(name)
        } else throw forbidden()
    }

    override fun createUser(user: NewUser): User {
        if (authorizationService.isSystem()) {
            logger.debug { "Creating new user: $user" }
            return userRepository.insert(user)
        } else throw forbidden()
    }

    override fun updateUser(user: User): User {
        if (authorizationService.isSystem()) {
            logger.debug { "Updating user: $user" }
            return userRepository.update(user)
        } else throw forbidden()
    }

    override fun deleteUser(id: Long) {
        if (authorizationService.isSystem()) {
            logger.debug { "Deleting user $id" }
            userRepository.delete(id)
        } else throw forbidden()
    }

    private fun forbidden(): ForbiddenException {
        return ForbiddenException("User is not authorized for this operation.")
            .also { logger.error { it.message } }
    }

    companion object: KLogging()

}
