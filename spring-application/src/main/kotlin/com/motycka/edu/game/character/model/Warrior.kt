package com.motycka.edu.game.character.model

internal class Warrior(
    name: String,
    health: Int,
    attackPower: Int,
    override var stamina: Int,
    override val defensePower: Int,
    override val level: CharacterLevel, // new
) : Character(
    name = name,
    health = health,
    attackPower = attackPower
), Defender {

    private val maxStamina = stamina

    // new
    init {
        val pointsAssigned = health + attackPower + stamina + defensePower
        require(pointsAssigned <= level.points) { "Attributes can not exceed ${level.points} level points (assigned $pointsAssigned)" }
        require(pointsAssigned == level.points) { "All ${level.points} level points must be assigned (assigned $pointsAssigned)." }
    }

    override fun attack(target: Character) {
        when {
            health <= 0 -> println("$name is dead and cannot attack")
            stamina <= 0 -> println("$name is too tired to attack")
            else -> {
                println("$name swings a sword at ${target.name}")
                target.receiveAttack(attackPower)
                stamina--
            }
        }
    }

    override fun receiveAttack(attackPower: Int) {
        super.receiveAttack(defend(health - attackPower))
    }

    override fun defend(attackPower: Int): Int {
        return if (stamina > 0) {
            println("$name raises shield and defends against $defensePower damage")
            attackPower - defensePower
        } else {
            println("$name is too tired to defend")
            attackPower
        }
    }

    override fun beforeRound() {
        if (stamina < maxStamina) {
            println("$name regenerates 1 stamina")
            stamina++
        }
    }

    override fun afterRound() {
        // no-op
    }
}
