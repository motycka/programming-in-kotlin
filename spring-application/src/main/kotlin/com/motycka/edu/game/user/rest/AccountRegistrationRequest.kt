package com.motycka.edu.game.user.rest

data class AccountRegistrationRequest(
    val name: String,
    val username: String,
    val password: String
)
