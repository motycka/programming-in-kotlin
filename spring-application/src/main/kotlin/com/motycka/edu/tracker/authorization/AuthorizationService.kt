package com.motycka.edu.tracker.authorization

import com.motycka.edu.tracker.config.ApplicationConfiguration
import com.motycka.edu.tracker.error.NotFoundException
import com.motycka.edu.tracker.user.UserRepository
import com.motycka.edu.tracker.user.model.User
import mu.KLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * AuthorizationService is responsible for validating authorization context.
 */
@Service
class AuthorizationService(
    private val userRepository: UserRepository
) {

    fun setSecurityContext(userId: Long) {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(userId, "N/A")
    }

    /**
     * Resolves current user context.
     * @return user as User
     */
    fun getCurrentUser(): User {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val user = userRepository.selectById(userId)

        return when (user) {
            null -> {
                logger.error("User not found for id: $userId")
                throw NotFoundException("User not found")
            }
            else -> user
        }
    }

    /**
     * Returns true if current user is SYSTEM user
     * @return boolean
     */
    fun isSystem(): Boolean {
        return SecurityContextHolder.getContext().authentication.principal == ApplicationConfiguration.SYSTEM_USER_ID
    }

    companion object: KLogging()
}
