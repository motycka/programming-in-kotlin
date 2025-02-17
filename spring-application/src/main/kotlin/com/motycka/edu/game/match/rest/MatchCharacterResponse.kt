package com.motycka.edu.game.match.rest

import com.motycka.edu.game.character.model.CharacterLevel
import com.motycka.edu.game.character.rest.CharacterClass
import com.motycka.edu.game.character.rest.CharacterId

data class MatchCharacterResponse(
    val id: CharacterId,
    val name: String,
    val characterClass: CharacterClass,
    val level: CharacterLevel,
    val experience: Int,
    val isVictor: Boolean
)
