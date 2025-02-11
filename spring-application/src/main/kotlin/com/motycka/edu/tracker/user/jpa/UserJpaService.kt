//package com.harbourspace.tracker.user.jpa
//
//import com.harbourspace.tracker.authorization.AuthorizationService
//import com.harbourspace.tracker.error.AuthorizationException
//import com.harbourspace.tracker.error.NotFoundException
//import com.harbourspace.tracker.user.UserService
//import com.harbourspace.tracker.user.model.NewUser
//import com.harbourspace.tracker.user.model.User
//import mu.KLogging
//import org.springframework.context.annotation.Primary
//import org.springframework.stereotype.Service
//import kotlin.jvm.optionals.getOrNull
//
///**
// * The UserJpaService uses the UserJpaRepository to perform CRUD operations on users.
// */
////@Primary
//@Service
//class UserJpaService(
//    private val userRepository: UserJpaRepository,
//    private val authorizationService: AuthorizationService
//) : UserService {
//
//    override fun getUsers(): List<User> {
//        if (authorizationService.isSystem()) {
//            logger.debug { "Getting all users" }
//            return userRepository.findAll().map { it.toUser() }
//        } else throw unauthorized()
//    }
//
//    override fun getUserById(id: Long): User {
//        if (authorizationService.isSystem()) {
//            logger.debug { "Getting user $id" }
//            return userRepository.findById(id).getOrNull()?.toUser() ?: throw userNotFound(id)
//        } else throw unauthorized()
//    }
//
//    override fun getUserByName(name: String): User {
//        if (authorizationService.isSystem()) {
//            logger.debug { "Getting user $name" }
//            return userRepository.findAllByName(listOf(name)).firstOrNull()?.toUser() ?: throw userNotFound(name)
//        } else throw unauthorized()
//    }
//
//    override fun createUser(user: NewUser): User {
//        if (authorizationService.isSystem()) {
//            logger.debug { "Creating new user: $user" }
//            return userRepository.save(user.fromUser()).toUser()
//        } else throw unauthorized()
//    }
//
//    override fun updateUser(user: User): User {
//        if (authorizationService.isSystem()) {
//            logger.debug { "Updating user: $user" }
//            return userRepository.save(user.fromUser()).toUser()
//        } else throw unauthorized()
//    }
//
//    override fun deleteUser(id: Long) {
//        if (authorizationService.isSystem()) {
//            logger.debug { "Deleting user $id" }
//            userRepository.delete(userRepository.getReferenceById(id))
//        } else throw unauthorized()
//    }
//
//    private fun unauthorized(): AuthorizationException {
//        return AuthorizationException("User is not authorized for this operation.")
//            .also { logger.error(it.message) }
//    }
//
//    private fun userNotFound(anyId: Any): NotFoundException {
//        return NotFoundException("User $anyId not found").also { logger.error(it.message) }
//    }
//
//    fun User.fromUser(): UserEntity {
//        return UserEntity().apply {
//            id = this.id
//            name = this.name
//        }
//    }
//
//    fun NewUser.fromUser(): UserEntity {
//        return UserEntity().apply {
//            name = this.name
//        }
//    }
//
//    fun UserEntity.toUser(): User {
//        return User(
//            id = requireNotNull(id),
//            name = requireNotNull(name)
//        )
//    }
//
//    companion object: KLogging()
//}
