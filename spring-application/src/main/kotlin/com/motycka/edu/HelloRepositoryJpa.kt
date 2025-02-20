package com.motycka.edu

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HelloRepositoryJpa : JpaRepository<MessageEntity, Long> {
    fun findByLocaleAndMessageKey(locale: String, messageKey: String): MessageEntity?
}

@Entity
@Table(name = "greeting")
data class MessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val locale: String,
    val messageKey: String,
    val messageValue: String
) {
    // No-argument constructor for Hibernate
    constructor() : this(null, "", "", "")
}
