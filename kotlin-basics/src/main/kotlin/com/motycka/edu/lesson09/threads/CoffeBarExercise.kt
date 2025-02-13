//package com.motycka.edu.lesson09.threads
//
//import io.github.oshai.kotlinlogging.KotlinLogging
//import java.util.concurrent.ConcurrentLinkedDeque
//import java.util.concurrent.atomic.AtomicInteger
//import kotlin.concurrent.thread
//
//private val logger = KotlinLogging.logger {}
//
//fun main() {
//    val coffeeBar = CoffeeBar()
//
//    repeat(10) {
//        coffeeBar.placeOrder(Coffee.entries.random())
//    }
//
//    coffeeBar.open()
//
//    repeat(10) {
//        coffeeBar.placeOrder(Coffee.entries.random())
//    }
//
//    coffeeBar.close()
//}
//
//private class CoffeeBar {
//
//    private val orderQueue = ConcurrentLinkedDeque<Coffee>()
//    private val ordersDone = AtomicInteger(0)
//
//    private val barista1 = Barista(name = "Pony", orderQueue = orderQueue, ordersDone = ordersDone)
//    private val barista2 = Barista(name = "Moni", orderQueue = orderQueue, ordersDone = ordersDone)
//
//    val baristaThread1 = thread(start = false, name = barista1.name) {
//        barista1.makeCoffee()
//    }
//
//    val baristaThread2 = thread(start = false, name = barista2.name) {
//        barista2.makeCoffee()
//    }
//
//    fun open() {
//        logger.info { "We're open!" }
//        baristaThread1.start()
//        baristaThread2.start()
//    }
//
//    fun close() {
//        try {
//            while (orderQueue.isNotEmpty()) {
//                logger.info { "Waiting for orders to be done" }
//                Thread.sleep(1000)
//            }
//            baristaThread1.join()
//            baristaThread2.join()
//        } catch (e: InterruptedException) {
//            Thread.currentThread().interrupt()
//            logger.warn { "Coffee bar was interrupted." }
//            throw RuntimeException(e)
//        } finally {
//            logger.info { "We're closed!" }
//            logger.info { "Orders in queue: ${orderQueue.size}" }
//            logger.info { "Orders done: ${ordersDone.get()}" }
//        }
//    }
//
//    fun placeOrder(coffee: Coffee) {
//        orderQueue.add(coffee)
//    }
//}
//
//private data class Barista(
//    val name: String,
//    private val orderQueue: ConcurrentLinkedDeque<Coffee>,
//    private val ordersDone: AtomicInteger,
//) {
//
//    fun makeCoffee() {
//        try {
//            while (orderQueue.isNotEmpty()) {
//                logger.debug() { "$name is ready to take an order" }
//                val order = orderQueue.poll()
//                logger.info { "$name is preparing $order" }
//                Thread.sleep(1000)
//                logger.info { "Your $order is ready" }
//                ordersDone.incrementAndGet()
//            }
//        } catch (e: InterruptedException) {
//            Thread.currentThread().interrupt()
//            logger.warn { "$name was interrupted." }
//            throw RuntimeException(e)
//        }
//    }
//}
//
//enum class Coffee {
//    BLACK_COFFEE,
//    LATTE,
//    CAPPUCCINO,
//    ESPRESSO,
//    DOUBLE_ESPRESSO,
//    AMERICANO,
//    MACCHIATO
//}
