package com.motycka.edu.lesson06

/*
Create the model classes for the Star Wars characters:
- Create a data class StarWarsCharacter with the properties name and fraction.
- Create an enum class Fraction with the values JEDI and SITH.
- Create an enum class DuelResult with the values WINNER and LOSER.
 */
data class StarWarsCharacter(
    val name: String,
    val fraction: Fraction
)

/*
Create enum classes for the Star Wars character fractions with values of JEDI and SITH.
 */
enum class Fraction {
    JEDI,
    SITH,
    REBEL,
    IMPERIAL,
}
