package com.motycka.edu.game.match.model

import com.motycka.edu.game.character.rest.CharacterId

data class MatchResult(
    val id: MatchId?,
    val challengerId: CharacterId,
    val opponentId: CharacterId,
    val victorId: CharacterId?,
)

