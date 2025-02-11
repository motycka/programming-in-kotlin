package com.motycka.edu.lesson06

import kotlin.collections.forEachIndexed

fun main() {

    val lightSide = starWarsCharacters.filter { it.fraction in setOf(Fraction.JEDI, Fraction.REBEL) }
    val darkSide = starWarsCharacters.filterNot { it.fraction in setOf(Fraction.JEDI, Fraction.REBEL) }

    val pairs = lightSide.zip(darkSide)
    val (dark, light) = pairs.unzip()

    pairs.zipWithNext { current, next ->
        listOf(
            current.first to next.first,
            current.second to next.second
        )
    }

    val results = pairs.matchWithFlatResults()

    val scoreboardByCharacter = results
        .groupBy { it.first.name }
        .map { (_, results) ->
            results.reduce { acc, (character, score) ->
                character to score + acc.second
            }

        }

    println("\nScoreboard by character:")
    scoreboardByCharacter.forEachIndexed { index, (character, score) ->
        println("[${index + 1}] ${character.name} - $score")
    }

    // using fold
    val scoreboardByFraction = results
        .fold(mutableMapOf<Fraction, Int>()) { acc, (character, score) ->
            acc[character.fraction] = acc.getOrDefault(character.fraction, 0) + score
            acc
        }.toList()

    println("\nScoreboard by fraction:")
    scoreboardByFraction.forEachIndexed { index, (fraction, score) ->
        println("[${index + 1}] ${fraction.name} - $score")
    }

}
