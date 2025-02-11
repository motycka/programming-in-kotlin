package com.motycka.edu.game

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

internal class Warrior(
    name: String,
    health: Int,
    attackPower: Int,
    override val stamina: Int,
    override val defensePower: Int,
    override val level: CharacterLevel, // new
) : Character(
    name = name,
    health = health,
    attackPower = attackPower
), Defender {

    private var currentStamina = stamina

    // new
    init {
        val pointsAssigned = health + attackPower + stamina + defensePower
        require(pointsAssigned <= level.points) { "Attributes can not exceed ${level.points} level points (assigned $pointsAssigned)" }
        require(pointsAssigned == level.points) { "All ${level.points} level points must be assigned (assigned $pointsAssigned)." }
    }

    override fun attack(target: Character) {
        when {
            health <= 0 -> logger.info { "$name is dead and cannot attack" }
            stamina <= 0 -> logger.info { "$name is too tired to attack" }
            else -> {
                logger.info { "$name swings a sword at ${target.name}" }
                target.receiveAttack(attackPower)
                currentStamina--
            }
        }
    }

    override fun receiveAttack(attackPower: Int) {
        super.receiveAttack(defend(health - attackPower))
    }

    override fun defend(attackPower: Int): Int {
        return if (stamina > 0) {
            logger.info { "$name raises shield and defends against $defensePower damage" }
            attackPower - defensePower
        } else {
            logger.info { "$name is too tired to defend" }
            attackPower
        }
    }

    override fun beforeRound() {
        if (currentStamina < stamina) {
            logger.info { "$name regenerates 1 stamina" }
            currentStamina++
        }
    }

    override fun afterRound() {
        // no-op
    }
}
