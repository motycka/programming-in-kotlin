package com.motycka.edu.lesson09.coroutines

import java.util.concurrent.Executors
import java.util.concurrent.Future

fun main() {
    val messenger = Messenger()
    val message: Future<String> = messenger.receiveMessage()

    while (message.isDone.not()) {
        println("Waiting for message...")

        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw RuntimeException(e)
        }
    }

    try {
        println("Received message: ${message.get()}")
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}

class Messenger {
    private val executor = Executors.newSingleThreadExecutor()

    fun receiveMessage(): Future<String> {
        return executor.submit<String> {
            Thread.sleep(3000)
            "Hello from future!"
        }
    }
}
