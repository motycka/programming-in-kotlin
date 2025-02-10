package com.motycka.edu.lesson06.game

internal data class MatchResult(
    val character1: Character,
    val character2: Character,
    val round: Int,
    val victor: Character?
)
