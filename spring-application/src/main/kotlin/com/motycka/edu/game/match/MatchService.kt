package com.motycka.edu.game.match

import com.motycka.edu.game.character.CharacterService
import com.motycka.edu.game.character.rest.CharactersFilter
import com.motycka.edu.game.character.model.Character
import com.motycka.edu.game.character.rest.CharacterId
import com.motycka.edu.game.match.model.MatchResult
import com.motycka.edu.game.match.model.MatchResultWithCharacters
import com.motycka.edu.game.match.model.MatchRoundResult
import com.motycka.edu.game.user.AccountService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
class MatchService(
    private val matchRepository: MatchRepository,
    private val characterService: CharacterService,
    private val accountService: AccountService
) {

    fun getMatches(): List<MatchResultWithCharacters> {
        val matches = matchRepository.selectMatches()
        val characters = characterService.getCharacters(
            CharactersFilter(
                ids = matches.flatMap { listOf(it.challengerId, it.opponentId) }.toSet(),
                includeChallengers = true,
                includeOpponents = true
            )
        )
        return matchRepository.selectMatches().map { match ->
            MatchResultWithCharacters(
                challenger = characters.find { it.characterId == match.challengerId }!!, // TODO
                opponent = characters.find { it.characterId == match.opponentId }!!, // TODO
                match = match,
                rounds = matchRepository.selectRounds(match.id!!), // TODO
                currentAccountId = accountService.getCurrentAccountId()
            )
        }
    }

    fun doMatch(
        rounds: Int,
        challengerId: CharacterId,
        opponentId: CharacterId
    ): MatchResultWithCharacters {
        return match(
            rounds = rounds,
            challenger = characterService.getCharacter(challengerId),
            opponent = characterService.getCharacter(opponentId)
        ).also { matchResult ->
            logger.info { "Match result: $matchResult" }
            matchRepository.insertMatch(matchResult.match)
        }
    }

    @Transactional
    fun match(
        rounds: Int,
        challenger: Character,
        opponent: Character
    ): MatchResultWithCharacters {
        var round = 0


        // TODO collect while conditon is true
        val roundResults = (0 until rounds).mapNotNull {
            if (challenger.getStats().health > 0 && opponent.getStats().health > 0) {
                round(round++, challenger, opponent)
            } else null // TODO
        }.flatten()

        val victor = when {
            challenger.getStats().health <= 0 && opponent.getStats().health > 0 -> {
                logger.info { ("\n${opponent.name} is the victor in round $round!") }
                opponent
            }
            opponent.getStats().health <= 0 && challenger.getStats().health > 0 -> {
                logger.info { "\n${challenger.name} is the victor in round $round!" }
                challenger
            }
            else -> {
                logger.info { "\nIt's a draw!" }
                null
            }
        }

        val matchResult = matchRepository.insertMatch(
            MatchResult(
                id = null,
                challengerId = challenger.characterId,
                opponentId = opponent.characterId,
                victorId = victor?.characterId,
            )
        )

        val rounds = roundResults.flatMap { roundResult ->
            matchRepository.insertRound(matchResult.id!!, roundResult)
        }

        return MatchResultWithCharacters(
            challenger = challenger,
            opponent = opponent,
            match = matchResult,
            rounds = rounds,
            currentAccountId = accountService.getCurrentAccountId()
        )
    }

    private fun round(round: Int, challenger: Character, opponent: Character): List<MatchRoundResult> {
        val challengerStatsBefore = challenger.getStats()
        val opponentStatsBefore = opponent.getStats()

        challenger.beforeRound()
        opponent.beforeRound()

        logger.info { "\nROUND $round" }
        challenger.attack(opponent)
        opponent.attack(challenger)

        // new
        challenger.afterRound()
        opponent.afterRound()

        val challengerStatsAfter = challenger.getStats()
        val opponentStatsAfter = opponent.getStats()

        return listOf(
            MatchRoundResult(
                id = null,
                round = round,
                characterId = challenger.characterId,
                healthDelta = challengerStatsBefore.health - challengerStatsAfter.health,
                staminaDelta = 0,
                manaDelta = challengerStatsBefore.mana - challengerStatsAfter.mana
            ),
            MatchRoundResult(
                id = null,
                round = round,
                characterId = opponent.characterId,
                healthDelta = opponentStatsBefore.health - opponentStatsAfter.health,
                staminaDelta = opponentStatsBefore.stamina - opponentStatsAfter.stamina,
                manaDelta = 0
            )
        )
    }

}
