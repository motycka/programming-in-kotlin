package com.motycka.edu.game

internal data class MatchResult(
    val character1: Character,
    val character2: Character,
    val round: Int,
    val victor: Character?
)
