package com.motycka.edu.game.user.model


data class Account(
    val id: AccountId?,
    val name: String,
    val username: String,
    val password: String
)
