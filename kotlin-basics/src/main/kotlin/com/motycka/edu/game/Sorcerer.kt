package com.motycka.edu.game

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

internal class Sorcerer(
    name: String,
    health: Int,
    attackPower: Int,
    override val mana: Int,
    override val healingPower: Int,
    override val level: CharacterLevel, // new
) : Character(
    name = name,
    health = health,
    attackPower = attackPower,
), Healer {

    private var currentMana: Int = mana

    // new
    init {
        val pointsAssigned = health + attackPower + mana + healingPower
        require(pointsAssigned <= level.points) { "Attributes can not exceed ${level.points} level points (assigned $pointsAssigned)" }
        require(pointsAssigned == level.points) { "All ${level.points} level points must be assigned (assigned $pointsAssigned)." }
    }

    override fun attack(target: Character) {
        heal()
        when {
            health <= 0 -> logger.info { "$name is dead and cannot attack" }
            mana <= 0 -> logger.info { "$name out of mana" }
            else -> {
                logger.info { "$name casts a spell at ${target.name}" }
                target.receiveAttack(attackPower)
                currentMana--
            }
        }
    }

    override fun heal() {
        when {
            health <= 0 -> logger.info { "$name is dead and cannot heal"}
            mana <= 0 -> logger.info { "$name is out of mana" }
            else -> {
                if (health + healingPower > health) {
                    currentHealth = health
                } else {
                    currentHealth += healingPower
                }
                println("$name heals self to $health health")
            }
        }
    }

    override fun beforeRound() {
        if (currentMana < mana) {
            logger.info { "$name regenerates 1 mana" }
            currentMana++
        }
    }

    override fun afterRound() {
        // no-op
    }

}
