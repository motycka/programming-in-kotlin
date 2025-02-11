package com.motycka.edu.lesson08

import mu.KotlinLogging
import java.io.File
import java.io.IOException
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

private val logger = KotlinLogging.logger {}

fun main() {

    loadData("docs/data/meteodata.csv")
        .getStatistics(ZoneId.of("Asia/Bangkok"))
        .onEach(::println)


}

fun Collection<Measurement>.getStatistics(atZone: ZoneId): List<Measurement> {
    return groupBy { measurement ->
        measurement.time.withZoneSameInstant(atZone).truncatedTo(ChronoUnit.DAYS)
    }.map { (time, measurements) ->
        Measurement(
            time = time,
            temperature = measurements.map { it.temperature }.average(),
            humidity = measurements.map { it.humidity }.average(),
            precipitation = measurements.sumOf { it.precipitation }
        )
    }.onEach(::println)
}


fun loadData(filePath: String): List<Measurement> {
    return try {
        val file = File(filePath)
        if (file.exists()) {
            logger.info { "Reading file: ${file.absolutePath}" }
            file.readLines().drop(1).map { it.toMeasurement() }
        } else {
            error("characters.csv file not found")
        }
    } catch (e: IOException) {
        error("An error occurred while reading the file: ${e.message}")
    } finally {
        logger.info { "Data successfully loaded." }
    }
}

data class Measurement(
    val time: ZonedDateTime,
    val temperature: Double,
    val humidity: Double,
    val precipitation: Double
)

fun String.toMeasurement(): Measurement {
    val (time, temperature, humidity, precipitation) = split(",").also { require(it.size == 4) }
    return Measurement(
        time = ZonedDateTime.parse(time),
        temperature = temperature.toDouble(),
        humidity = humidity.toDouble(),
        precipitation = precipitation.toDouble()
    )
}
