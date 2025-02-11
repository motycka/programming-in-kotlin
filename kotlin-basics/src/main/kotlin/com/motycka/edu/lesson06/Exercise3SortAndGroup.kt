package com.motycka.edu.lesson06

fun main() {

    val lightSide = starWarsCharacters.filter { it.fraction in setOf(Fraction.JEDI, Fraction.REBEL) }
    val darkSide = starWarsCharacters.filterNot { it.fraction in setOf(Fraction.JEDI, Fraction.REBEL) }


}
