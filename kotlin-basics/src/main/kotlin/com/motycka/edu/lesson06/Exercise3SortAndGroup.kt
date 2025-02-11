package com.motycka.edu.lesson06

fun main() {

    val lightSide = starWarsCharacters.filter { it.fraction in setOf(Fraction.JEDI, Fraction.REBEL) }
    val darkSide = starWarsCharacters.filterNot { it.fraction in setOf(Fraction.JEDI, Fraction.REBEL) }

    val results = createPairs(
        fraction1 = lightSide,
        fraction2 = darkSide
    ).matchWithFlatResults()

    val padding1 = results.maxOf { (character, _) -> character.name.length }
    val padding2 = Fraction.entries.maxOf { it.name.length }

    println("Scores by fraction:")
    val scoresByCharacter = results
        .groupBy { (character, _) -> character }
        .map { (character, scores) -> character to scores.sumOf { (_, score) -> score } }
        .sortedByDescending { (_, score) -> score }
        .onEachIndexed { index, (character, score) ->
            println("[${(index + 1).toString().padStart(2, '0')}] ${character.name.padTo(padding1)} $score")
        }


    println("\n\nScores by fraction:")
    val scoresByFraction = results
        .groupBy { (character, _) -> character.fraction }
        .map { (character, scores) -> character to scores.sumOf { (_, score) -> score } }
        .sortedByDescending { (_, score) -> score }
        .onEachIndexed { index, (fraction, score) ->
            println("[${(index + 1).toString().padStart(2, '0')}] ${fraction.name.padTo(padding2)} $score")
        }
}

private fun String.padTo(length: Int): String {
    return this + " ".repeat(length - this.length) + "   "
}
