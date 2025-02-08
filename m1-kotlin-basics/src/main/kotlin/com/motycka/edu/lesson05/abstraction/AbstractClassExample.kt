package com.motycka.edu.lesson05.abstraction

fun main() {
     match(
        character1 = Warrior(
            name = "Thorne Ironfist",
            health = 100,
            attackPower = 10,
            stamina = 10
        ),
        character2 = Sorcerer(
            name = "Eldrin Starfire",
            health = 50,
            attackPower = 20,
            mana = 5
        )
     )
}

internal fun match(character1: Character, character2: cCharacter) {
    var round = 0
    while (character1.health > 0 && character2.health > 0 && round < 10) {
        round++
        println("\nROUND $round:")
        character1.attack(character2)
        character2.attack(character1)
    }

    when {
        character1.health <= 0 && character2.health > 0 -> println("\n${character2.name} is the victor in round $round!")
        character2.health <= 0 && character1.health > 0 -> println("\n${character2.name} is the victor in round $round!")
        else -> println("\nIt's a draw!")
    }
}

internal abstract class Character(
    val name: String,
    var health: Int,
    val attackPower: Int
) {
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

internal class Warrior(
    name: String,
    health: Int,
    attackPower: Int,
    private var stamina: Int
) : Character(
    name = name,
    health = health,
    attackPower = attackPower
) {

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
}

internal class Sorcerer(
    name: String,
    health: Int,
    attackPower: Int,
    private var mana: Int
) : Character(
    name = name,
    health = health,
    attackPower = attackPower,
) {
    override fun attack(target: Character) {
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
}

