package com.motycka.edu.game

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

internal abstract class Character(
    val name: String,
    val health: Int,
    val attackPower: Int
): Recoverable { // new

    protected var currentHealth: Int = health // new

    // new
    abstract val level: CharacterLevel

    abstract fun attack(target: Character)

    open fun receiveAttack(attackPower: Int) {
        when {
            currentHealth - attackPower > 0 -> {
                currentHealth -= attackPower
                logger.info { "$name has $currentHealth health remaining." }
            }

            else -> {
                currentHealth = 0
                logger.info { "$name has been defeated" }
            }
        }
    }
}
