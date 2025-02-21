package com.motycka.edu

import org.springframework.context.ApplicationEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class HelloEventListener {

    @EventListener
    fun handleHelloEvent(event: HelloEvent) {
        println("Received event: ${event.message}")
    }
}


data class HelloEvent(
    val message: String
) : ApplicationEvent(message)
