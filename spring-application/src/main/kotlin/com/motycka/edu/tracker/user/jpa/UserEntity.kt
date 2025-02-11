package com.motycka.edu.tracker.user.jpa

import jakarta.persistence.*

/**
 * This is example database entity.
 */
@Entity
@Table(name = "users")
class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var name: String? = null
}
