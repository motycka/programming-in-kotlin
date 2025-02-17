package com.motycka.edu.game.user

import com.motycka.edu.game.user.model.Account
import com.motycka.edu.game.user.model.AccountId
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

/**
 * The UserJdbcService uses the UserJdbcRepository to perform CRUD operations on users.
 */
@Service
class AccountService(
    private val userRepository: AccountRepository,
) {

    fun getCurrentAccountId(): AccountId {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication.principal
        return if (principal is UserDetails) {
            userRepository.selectByUsername(principal.username)?.id ?: throw UsernameNotFoundException(principal.username)
        } else {
            error("Unknown principal type: $principal")
        }
    }

    fun getByUsername(username: String): Account? {
        logger.debug { "Getting user $username" }
        return userRepository.selectByUsername(username)
    }

    fun createAccount(account: Account): Account {
        logger.debug { "Creating new user: $account" }
        return userRepository.insert(account) ?: error(CREATE_ERROR)
    }

    companion object {
        const val CREATE_ERROR = "Account could not be created."
    }
}
