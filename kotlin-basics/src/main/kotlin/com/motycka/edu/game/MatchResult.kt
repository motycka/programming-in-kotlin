package com.motycka.edu.game

internal data class MatchResult(
    val challenger: Character,
    val opponent: Character,
    val round: Int,
    val victor: Character?
)

internal data class RoundResult(
    val round: Int,
    val challenger: Character,
    val opponent: Character
)

internal data class CharacterState(
    val character: Character,
    val health: Int,
    val stamina: Int,
    val mana: Int
)
