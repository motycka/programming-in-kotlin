package com.motycka.edu.lesson06

fun main() {

    val lightSide = starWarsCharacters.filter { it.fraction in setOf(Fraction.JEDI, Fraction.REBEL) }
    val darkSide = starWarsCharacters.filterNot { it.fraction in setOf(Fraction.JEDI, Fraction.REBEL) }

    createPairs(
        fraction1 = lightSide,
        fraction2 = darkSide
    ).matchWithFlatResults()
        .groupBy { (character, _) -> character }
        .map { (character, scores) -> character to scores.sumOf { (_, score) -> score } }
        .sortedByDescending { (_, score) -> score }
        .forEachIndexed { index, (character, score) ->
            println("[${index + 1}] ${character.name} - $score")
        }
}
