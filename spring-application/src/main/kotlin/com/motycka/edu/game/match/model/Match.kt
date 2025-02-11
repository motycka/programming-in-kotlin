package com.motycka.edu.game.match.model

import com.motycka.edu.game.character.model.Character

// new
internal data class Match(
    val rounds: Int,
    val challenger: Character,
    val opponent: Character
) {

    internal fun fight(): Character? {
        var round = 0
        while (challenger.health > 0 && opponent.health > 0 && round < rounds) {
            // new
            challenger.beforeRound()
            opponent.beforeRound()

            round++
            println("\nROUND $round:")
            challenger.attack(opponent)
            opponent.attack(challenger)

            // new
            challenger.afterRound()
            opponent.afterRound()
        }

        return when {
            challenger.health <= 0 && opponent.health > 0 -> {
                println("\n${opponent.name} is the victor in round $round!")
                opponent
            }
            opponent.health <= 0 && challenger.health > 0 -> {
                println("\n${challenger.name} is the victor in round $round!")
                challenger
            }
            else -> {
                println("\nIt's a draw!")
                null
            }
        }
    }

}

