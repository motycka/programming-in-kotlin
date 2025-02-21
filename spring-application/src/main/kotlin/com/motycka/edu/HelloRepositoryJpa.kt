package com.motycka.edu

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HelloRepositoryJpa : JpaRepository<HelloEntity, Long> {
    fun findByLocale(locale: String): HelloEntity?
}

@Entity
@Table(name = "hello")
data class HelloEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val locale: String,
    val hello: String
) {
    // No-argument constructor for Hibernate
    constructor() : this(null, "", "")
}
