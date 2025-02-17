package com.motycka.edu

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
class GameApplication

fun main(args: Array<String>) {
    runApplication<GameApplication>(*args)
}
