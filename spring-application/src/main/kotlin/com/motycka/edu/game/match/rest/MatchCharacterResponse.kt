package com.motycka.edu.game.match.rest

import com.motycka.edu.game.character.rest.CharacterResponse

data class MatchCharacterResponse(
    val character: CharacterResponse,
    val isVictor: Boolean
)
