package com.motycka.edu.game.match.rest

import com.motycka.edu.game.character.model.Character
import com.motycka.edu.game.character.rest.getClass
import com.motycka.edu.game.match.model.MatchResultWithCharacters

fun MatchResultWithCharacters.toMatchResultTo() = MatchResponse(
    id = requireNotNull(match.id) { "Match id is required" },
    challenger = challenger.toMatchCharacterTo(isVictor = challenger.characterId == match.victorId,),
    opponent = opponent.toMatchCharacterTo(isVictor = opponent.characterId == match.victorId),
    rounds = rounds.map { round ->
        MatchRoundResponse(
            round = round.round,
            characterId = round.characterId,
            healthDelta = round.healthDelta,
            staminaDelta = round.staminaDelta,
            manaDelta = round.manaDelta
        )
    }
)

fun List<MatchResultWithCharacters>.toMatchResultTos() = map {
    it.toMatchResultTo()
}

fun Character.toMatchCharacterTo(isVictor: Boolean) = MatchCharacterResponse(
    id = requireNotNull(id) { "Character id must not be null." },
    name = name,
    characterClass = getClass(),
    level = level,
    experience = experience,
    isVictor = isVictor
)

