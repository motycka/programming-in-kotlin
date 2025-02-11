package com.motycka.edu.game.character.model

internal class Sorcerer(
    name: String,
    health: Int,
    attackPower: Int,
    override var mana: Int,
    override val healingPower: Int,
    override val level: CharacterLevel, // new
) : Character(
    name = name,
    health = health,
    attackPower = attackPower,
), Healer {

    private val maxHealth = health
    private val maxMana = mana

    // new
    init {
        val pointsAssigned = health + attackPower + mana + healingPower
        require(pointsAssigned <= level.points) { "Attributes can not exceed ${level.points} level points (assigned $pointsAssigned)" }
        require(pointsAssigned == level.points) { "All ${level.points} level points must be assigned (assigned $pointsAssigned)." }
    }

    override fun attack(target: Character) {
        heal()
        when {
            health <= 0 -> println("$name is dead and cannot attack")
            mana <= 0 -> println("$name out of mana")
            else -> {
                println("$name casts a spell at ${target.name}")
                target.receiveAttack(attackPower)
                mana--
            }
        }
    }

    override fun heal() {
        when {
            health <= 0 -> println("$name is dead and cannot heal")
            mana <= 0 -> println("$name is out of mana")
            else -> {
                if (health + healingPower > maxHealth) {
                    health = maxHealth
                } else {
                    health += healingPower
                }
                println("$name heals self to $health health")
            }
        }
    }

    override fun beforeRound() {
        if (mana < maxMana) {
            println("$name regenerates 1 mana")
            mana++
        }
    }

    override fun afterRound() {
        // no-op
    }

}
