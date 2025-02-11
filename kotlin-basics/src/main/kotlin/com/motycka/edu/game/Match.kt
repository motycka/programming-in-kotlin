package com.motycka.edu.game

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

// new
internal data class Match(
    val rounds: Int,
    val challenger: Character,
    val opponent: Character
) {

    fun fight(): MatchResult {
        var round = 0
        while (challenger.health > 0 && opponent.health > 0 && round < rounds) {
            // new
            challenger.beforeRound()
            opponent.beforeRound()

            round++
            logger.info { "\nROUND $round" }
            challenger.attack(opponent)
            opponent.attack(challenger)

            // new
            challenger.afterRound()
            opponent.afterRound()
        }

        val victor = when {
            challenger.health <= 0 && opponent.health > 0 -> {
                logger.info { ("\n${opponent.name} is the victor in round $round!") }
                opponent
            }
            opponent.health <= 0 && challenger.health > 0 -> {
                logger.info { "\n${challenger.name} is the victor in round $round!" }
                challenger
            }
            else -> {
                logger.info { "\nIt's a draw!" }
                null
            }
        }

        return MatchResult(
            challenger = challenger,
            opponent = opponent,
            round = round,
            victor = victor
        )
    }

}

