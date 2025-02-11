package com.motycka.edu.lesson07

import mu.KotlinLogging
import java.io.File
import java.io.IOException

private val logger = KotlinLogging.logger {}

fun main() {

    val directoryPath = "./"
    val filePath = "$directoryPath/text.txt"

    try {
        val directory = File(directoryPath)
        val file = File(filePath)

        when {
            directory.exists().not() -> error("$directoryPath does not exist")
            directory.isFile -> error("$directoryPath is not a directory")
            directory.canRead().not() -> error("$directoryPath is not readable")
            directory.canWrite().not() -> error("$directoryPath is not writable")
        }

        if (file.exists().not()) {
            file.createNewFile()
            file.setReadOnly()
        }

        println("Full path: ${file.absolutePath}")
        println("Can read: ${file.canRead()}")
        println("Can write: ${file.canWrite()}")
        println("Can execute: ${file.canExecute()}")

        file.delete()

    } catch (e: IOException) {
        error("An error occurred while reading the file: ${e.message}")
    } finally {
        logger.info { "Characters successfully loaded." }
    }
}
