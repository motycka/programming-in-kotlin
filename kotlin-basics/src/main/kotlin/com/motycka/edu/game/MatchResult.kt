package com.motycka.edu.game

internal data class MatchResult(
    val challenger: Character,
    val opponent: Character,
    val round: Int,
    val victor: Character?
)
