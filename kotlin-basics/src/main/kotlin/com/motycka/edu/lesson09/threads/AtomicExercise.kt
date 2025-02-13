package com.motycka.edu.lesson09.threads

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

private val logger = KotlinLogging.logger {}

fun main() {
    val orders = AtomicInteger(10)

    thread(name = "Pony") { makeCoffee(orders) }
    thread(name = "Moni") { makeCoffee(orders) }
}

private fun makeCoffee(orders: AtomicInteger) {
    while (orders.decrementAndGet() >= 0) {
        try {
            logger.info { "${Thread.currentThread().name} is making your coffee." }
            Thread.sleep(5000)
            logger.info { "Your coffee is ready. Enjoy!" }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            logger.warn { "${Thread.currentThread().name} was interrupted." }
            throw RuntimeException(e)
        }
    }
}
