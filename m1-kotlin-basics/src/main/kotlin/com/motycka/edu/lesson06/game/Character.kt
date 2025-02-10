package com.motycka.edu.lesson06.game

internal abstract class Character(
    val name: String,
    var health: Int,
    val attackPower: Int,
): Recoverable { // new

    // new
    abstract val level: CharacterLevel

    abstract fun attack(target: Character)

    open fun receiveAttack(attackPower: Int) {
        when {
            health - attackPower > 0 -> {
                health -= attackPower
                println("$name has $health health remaining.")
            }

            else -> {
                health = 0
                println("$name has been defeated")
            }
        }
    }
}
