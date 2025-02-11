package com.motycka.edu.lesson07

import com.motycka.edu.lesson06.*
import mu.KotlinLogging
import java.io.File
import java.io.IOException

private val logger = KotlinLogging.logger {}

typealias Results = List<List<Pair<Int, List<Pair<StarWarsCharacter, Int>>>>>

fun main() {

    val characters = loadStarWarsCharacters("characters.csv")

    val pairs = createPairs(
        fraction1 = characters.filter { it.fraction in setOf(Fraction.JEDI, Fraction.REBEL) },
        fraction2 = characters.filter { it.fraction in setOf(Fraction.SITH, Fraction.IMPERIAL) }
    )

    val resultsFile = saveResults(
        results = pairs.match(),
        path = "results.txt"
    )

    logger.info { "Results are available in ${resultsFile.absolutePath}" }

}

fun loadStarWarsCharacters(fileName: String): List<StarWarsCharacter> {
    val uri = object {}.javaClass.getResource(fileName)?.toURI()
        ?: error("$fileName file not found")

    return try {
        val file = File(uri)

        if (file.exists()) {
            logger.info { "Reading file: ${file.absolutePath}" }

            file.readLines().drop(1).map { it.toStarWarsCharacter() }

        } else {
            error("characters.csv file not found")
        }
    } catch (e: IOException) {
        error("An error occurred while reading the file: ${e.message}")
    } finally {
        logger.info { "Characters successfully loaded." }
    }
}

fun String.toStarWarsCharacter(): StarWarsCharacter {
    val (name, fraction) = split(",").also { require(it.size == 2) }
    return StarWarsCharacter(
        name = name,
        fraction = Fraction.valueOf(fraction)
    )
}

fun saveResults(results: Results, path: String): File {
    try {
        val file = File(path)

        if (file.exists()) {
            logger.warn { "File ${file.absolutePath} already exists and will be overwritten!" }
        } else {
            logger.info { "Creating file ${file.absolutePath}." }
        }

        file.bufferedWriter().use { writer ->
            results.forEach { round ->
                writer.write(round.toString())
            }
        }

        return file

    } catch (e: IOException) {
        error("An error occurred while writing file: ${e.message}")
    } finally {
        logger.info { "Results successfully saved." }
    }
}
