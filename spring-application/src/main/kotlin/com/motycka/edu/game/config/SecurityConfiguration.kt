package com.motycka.edu.game.config

import com.motycka.edu.game.account.AccountService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration(private val userService: AccountService) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth.anyRequest().authenticated()
            }
            .formLogin { form ->
                form.loginPage("/login.html").permitAll()
            }
            .logout { logout ->
                logout.permitAll()
            }
//            .httpBasic { httpBasicCustomizer ->
//                httpBasicCustomizer.realmName("Fantasy.Space")
//            }

        return http.build()
    }

    @Bean
    fun userDetailsService() = UserDetailsService { username ->
        val user = userService.getByUsername(username)
            ?: throw UsernameNotFoundException("User not found")

        User.builder()
            .username(user.username)
            .password(passwordEncoder().encode(user.password))
            .roles("USER")
            .build()
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
