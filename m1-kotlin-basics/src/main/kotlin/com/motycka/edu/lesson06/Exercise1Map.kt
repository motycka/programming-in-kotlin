package com.motycka.edu.lesson06

val jediNames = listOf(
    "Luke Skywalker",
    "Yoda",
    "Obi-Wan Kenobi",
    "Mace Windu",
    "Qui-Gon Jinn"
)

val sithNames = listOf(
    "Darth Vader",
    "Emperor Palpatine",
    "Darth Maul",
    "Kylo Ren",
    "Count Dooku"
)

val rebelNames = listOf(
    "Leia Organa",
    "Han Solo",
    "Chewbacca",
    "C3PO",
    "R2D2"
)

val imperialNames = listOf(
    "Stormtrooper",
    "Imperial Officer",
    "Imperial Guard",
    "Death Trooper",
    "TIE Fighter Pilot"
)

fun main() {

    val pairs = createPairs(
        fraction1 = rebelNames.toFractionCharacters(Fraction.REBEL) + jediNames.toFractionCharacters(Fraction.JEDI),
        fraction2 = imperialNames.toFractionCharacters(Fraction.IMPERIAL) + sithNames.toFractionCharacters(Fraction.SITH)
    )

    pairs.match().onEach(::println)

    pairs.matchWithFlatResults().onEach(::println)
}

fun List<String>.toFractionCharacters(
    fraction: Fraction
): List<StarWarsCharacter> {
    return this.map { name ->
        StarWarsCharacter(name, fraction)
    }
}

fun createPairs(
    fraction1: List<StarWarsCharacter>,
    fraction2: List<StarWarsCharacter>,
): List<Pair<StarWarsCharacter, StarWarsCharacter>> {
    return fraction1.mapIndexed { index, character1 ->
        val character2 = fraction2.getOrElse(index) { error("No pair found for $character1") }
        character1 to character2
    }
}

fun round(
    character1: StarWarsCharacter,
    character2: StarWarsCharacter
): List<Pair<StarWarsCharacter, Int>> {
    val winner = setOf(character1, character2).random()
    val loser = if (winner == character1) character2 else character1
    return listOf(
        winner to 1,
        loser to 0
    )
}

fun List<Pair<StarWarsCharacter, StarWarsCharacter>>.match(): List<List<Pair<Int, List<Pair<StarWarsCharacter, Int>>>>> {
    return map { (character1, character2) ->
        (1..3).map { round ->
            round to round(character1, character2)
        }
    }
}

fun List<Pair<StarWarsCharacter, StarWarsCharacter>>.matchWithFlatResults(): List<Pair<StarWarsCharacter, Int>> {
    return flatMap { (character1, character2) ->
        (1..3).flatMap { round ->
            round(character1, character2)
        }
    }
}
