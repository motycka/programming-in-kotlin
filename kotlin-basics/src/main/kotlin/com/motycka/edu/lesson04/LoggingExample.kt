package com.motycka.edu.lesson04

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger { }

fun main() {

    logger.error { "error message" }
    logger.warn { "warn message" }
    logger.info { "info message" }
    logger.debug { "debug message" }
    logger.trace { "trace message" }

}
