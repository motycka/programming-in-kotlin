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
        .sortedBy { (_, score) -> score }
        .reversed()
        .forEach { (character, score) -> println("${character.name} - $score") }


    val priceList = listOf(
        "Mango" to 20,
        "Apple" to 25,
        "Banana" to 10,
        "Coconut" to 15,
        "Pineapple" to 30,
        "Orange" to 5,
        "Grapes" to 40
    )

    val priceCategory = priceList.groupBy { (_, price) ->
        when (price) {
            in 0..10 -> "Cheap"
            in 11..20 -> "Affordable"
            in 21..30 -> "Expensive"
            else -> "Very Expensive"
        }
    }

    priceCategory.forEach { println(it)  }

    priceList.sortedBy { (_, price) -> price }

    priceList.sortedByDescending { (_, price) -> price }

    priceList.sortedWith(
        compareBy(
            { it.first },
            { it.second }
        )
    )

}
