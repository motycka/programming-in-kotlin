package com.motycka.edu.lesson09.coroutines

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
