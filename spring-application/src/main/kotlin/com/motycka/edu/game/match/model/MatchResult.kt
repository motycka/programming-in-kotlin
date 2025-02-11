package com.motycka.edu.game.match.model

import com.motycka.edu.game.character.model.Character

internal data class MatchResult(
    val character1: Character,
    val character2: Character,
    val round: Int,
    val victor: Character?
)
