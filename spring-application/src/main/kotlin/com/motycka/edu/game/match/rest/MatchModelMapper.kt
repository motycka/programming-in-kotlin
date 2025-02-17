package com.motycka.edu.game.match.rest

import com.motycka.edu.game.character.model.Character
import com.motycka.edu.game.character.rest.toCharacterResponse
import com.motycka.edu.game.match.model.MatchResultWithCharacters
import com.motycka.edu.game.user.model.AccountId

fun MatchResultWithCharacters.toMatchResultTo() = MatchResponse(
    id = requireNotNull(match.id) { "Match id is required" },
    challenger = challenger.toMatchCharacterTo(
        isVictor = challenger.characterId == match.victorId,
        currentAccountId = currentAccountId
    ),
    opponent = opponent.toMatchCharacterTo(
        isVictor = opponent.characterId == match.victorId,
        currentAccountId = currentAccountId
    ),
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

fun Character.toMatchCharacterTo(isVictor: Boolean, currentAccountId: AccountId) = MatchCharacterResponse(
    character = toCharacterResponse(currentAccountId),
    isVictor = isVictor
)

