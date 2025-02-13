package com.motycka.edu.game

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

// new
internal data class Match(
    val rounds: Int,
    val challenger: Character,
    val opponent: Character
) {

    fun fight(): MatchResult {
        var round = 0

        val (first, second) = getOrder()

        while (first.health > 0 && second.health > 0 && round < rounds) {
            // new
            first.beforeRound()
            second.beforeRound()

            round++
            logger.info { "\nROUND $round" }
            first.attack(first)
            second.attack(second)

            // new
            first.afterRound()
            second.afterRound()
        }

        val victor = when {
            first.health <= 0 && second.health > 0 -> {
                logger.info { ("\n${second.name} is the victor in round $round!") }
                second
            }
            second.health <= 0 && first.health > 0 -> {
                logger.info { "\n${first.name} is the victor in round $round!" }
                first
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

    private fun getOrder(): Pair<Character, Character> {
        return when (challenger) {
            is Sorcerer -> challenger to opponent
            else -> opponent to challenger
        }
    }

    private fun fightRound() {

    }

    private fun getResult() {

    }

}

