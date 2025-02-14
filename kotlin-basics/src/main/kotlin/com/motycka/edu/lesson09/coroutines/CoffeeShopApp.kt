@file:OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package com.motycka.edu.lesson09.coroutines

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

private val logger = KotlinLogging.logger {}

fun main(): Unit = runBlocking {
    val coffeeShop = CoffeeShop(
        openDuration = 10,
        unit = ChronoUnit.SECONDS,
        baristas = setOf("Pony", "Moni"),
    )

    val orderGenerator = OrderGenerator(coffeeShop)

    coroutineScope {
        launch { orderGenerator.generateOrders() }
        launch { coffeeShop.processOrders() }
    }
}


class OrderGenerator(private val coffeeShop: CoffeeShop) {
    suspend fun generateOrders() {
        while (coffeeShop.isOpen()) {
            val order = MenuItem.entries.random()
            coffeeShop.placeOrder(order)
            delay(500)
        }
        logger.info { "Order generation stopped. Shop is closing." }
    }
}

class CoffeeShop(
    val openDuration: Long,
    val unit: ChronoUnit,
    val baristas: Set<String>
) {
    private val closingTime: ZonedDateTime = ZonedDateTime.now().plus(openDuration, unit)
    private val coffeeMachine = Mutex(locked = false)
    private val ordersChannel = Channel<MenuItem>(capacity = baristas.size)

    init {
        logger.info { "We are now open" }
    }

    fun isOpen(): Boolean = closingIn() > 0

    fun closingIn(): Long = ChronoUnit.SECONDS.between(ZonedDateTime.now(), closingTime)

    fun placeOrder(item: MenuItem): Boolean {
        val remainingTime = closingIn()
        return when {
            remainingTime > 5 -> acceptOrder(item)
            remainingTime in 1..5 -> {
                logger.warn { "We are closing soon, taking last orders ..." }
                acceptOrder(item)
            }
            else -> {
                logger.info { "Sorry, we're closed" }
                false
            }
        }
    }

    suspend fun processOrders() = coroutineScope {
        val jobs = baristas.map { barista ->
            launch { processOrdersForBarista(barista) } // Each barista works in parallel
        }

        val remainingTime = ChronoUnit.MILLIS.between(ZonedDateTime.now(), closingTime)
        if (remainingTime > 0) {
            delay(remainingTime)
        }

        ordersChannel.close()
        logger.info { "We are no longer accepting orders." }

        jobs.forEach { it.join() }
        logger.info { "We are now closed" }
    }

    private suspend fun processOrdersForBarista(barista: String) {
        for (order in ordersChannel) { // Each barista gets one order at a time
            logger.info { "$barista is processing order for $order" }

            if (coffeeMachine.isLocked) {
                logger.warn { "$barista is waiting for the coffee machine..." }
            }

            coffeeMachine.withLock {
                delay(order.time + TIME_TO_RECHARGE) // Simulating coffee preparation
                logger.info { "$order by $barista is ready" }
            }
        }
        logger.info { "$barista has finished processing all orders." }
    }

    private fun acceptOrder(item: MenuItem): Boolean {
        logger.info { "Accepting order for $item" }
        val accepted = ordersChannel.trySend(item).isSuccess
        if (ordersChannel.trySend(item).isSuccess) {
            logger.warn { "Baristas are busy! Could not accept order for $item." }
        }
        return accepted
    }

    companion object {
        const val TIME_TO_RECHARGE = 200
    }
}

enum class MenuItem(val time: Long) {
    AMERICANO(200),
    ESPRESSO(100),
    DOUBLE_ESPRESSO(200),
    CAPUCCINO(400),
    FLAT_WHITE(400),
}
