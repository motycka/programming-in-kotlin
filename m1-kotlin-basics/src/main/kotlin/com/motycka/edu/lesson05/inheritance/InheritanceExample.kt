package com.motycka.edu.lesson05.inheritance

fun main() {
    match(
        character1 = Warrior(
            name = "Thorne Ironfist",
            health = 100,
            attackPower = 10
        ),
        character2 = Sorcerer(
            name = "Eldrin Starfire",
            health = 50,
            attackPower = 20
        )
    )
}

internal fun match(character1: Character, character2: Character) {
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

internal open class Character(
    val name: String,
    var health: Int,
    val attackPower: Int
) {

    open fun attack(target: Character) {
        when {
            health <= 0 -> println("$name is dead and cannot attack")
            else -> {
                println("$name attacks ${target.name}")
                target.receiveAttack(attackPower)
            }
        }
    }

    open fun receiveAttack(attackPower: Int) {
        when {
            health - attackPower > 0 -> {
                health -= attackPower
                println("$name has $health health remaining")
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
    attackPower: Int
) : Character(
    name = name,
    health = health,
    attackPower = attackPower
)

internal class Sorcerer(
    name: String,
    health: Int,
    attackPower: Int
) : Character(
    name = name,
    health = health,
    attackPower = attackPower,
)

internal class Archer(name: String): Character(
    name = name,
    health = 100,
    attackPower = 10
)
