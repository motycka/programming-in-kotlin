package com.motycka.edu.game.error

import org.springframework.security.core.AuthenticationException

class AccessException(message: String = "Unauthorized access.") : AuthenticationException(message)
