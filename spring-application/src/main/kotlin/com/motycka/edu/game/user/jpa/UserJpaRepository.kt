//package com.harbourspace.tracker.user.jpa
//
//import com.motycka.edu.tracker.user.jpa.UserEntity
//import org.springframework.data.jpa.repository.JpaRepository
//import org.springframework.data.jpa.repository.Query
//import org.springframework.stereotype.Repository
//
///**
// * This is an example of repository implementation using Spring Data JPA
// */
//@Repository
//interface UserJpaRepository : JpaRepository<UserEntity, Long> {
//
////    Optional<UserEntity> findByName(String name);
//    @Query("SELECT u FROM UserEntity u WHERE u.username IN :username")
//    fun findAllByName(name: List<String>): List<UserEntity>
//
//}
//
