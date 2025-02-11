package com.motycka.edu.tracker.config

import com.motycka.edu.tracker.authorization.AuthorizationService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import java.lang.Long.parseLong
import kotlin.jvm.Throws

/**
 * No that this is not standard or common security configuration in Spring Boot. In real world, you would use
 * different authentication and authorization mechanisms, like JWT, OAuth, etc.
 * This is very simple and naive implementation, for the purpose of this exercise.
 */
@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val authorizationService: AuthorizationService
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(
        http: HttpSecurity,
        authorizationManager: AuthorizationManager<RequestAuthorizationContext>
    ): SecurityFilterChain {
        return http
            .csrf {
                it.disable() // should not be disabled in real world application
            }
            .authorizeHttpRequests { request ->
                request
                    .anyRequest()
                    .access(authorizationManager)
            }
        .build()
    }

    @Bean
    fun authorizationManager(): AuthorizationManager<RequestAuthorizationContext> {
        return AuthorizationManager<RequestAuthorizationContext> { authentication, context ->
            val authHeader = context.request.getHeader("Authorization")
            val isGranted = authHeader.startsWith(BASIC)

            if (isGranted) {
                val userId = parseLong(authHeader.replace(BASIC, ""))
                authorizationService.setSecurityContext(userId)
            }

            AuthorizationDecision(isGranted)
        }
    }

    companion object {
        private const val BASIC = "Basic "
    }

}
