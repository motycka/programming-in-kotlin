package com.motycka.edu.game.user.rest

import com.motycka.edu.game.user.model.Account

fun AccountRegistrationRequest.toAccount() = Account(
    id = null,
    name = name,
    username = username,
    password = password
)
