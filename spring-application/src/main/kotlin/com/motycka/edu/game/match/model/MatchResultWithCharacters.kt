package com.motycka.edu.game.match.model

import com.motycka.edu.game.character.model.Character

data class MatchResultWithCharacters(
    val challenger: Character,
    val opponent: Character,
    val match: MatchResult,
    val rounds: List<MatchRoundResult>
)
