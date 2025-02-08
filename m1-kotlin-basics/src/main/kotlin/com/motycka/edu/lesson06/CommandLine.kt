package com.motycka.edu.lesson06

import com.motycka.edu.lesson05.interfaces.Sorcerer
import com.motycka.edu.lesson05.interfaces.Warrior
import com.motycka.edu.lesson05.interfaces.match

fun main(args: Array<String>) {

//    println("Received ${args.size} arguments:")
//
//    for ((index, arg) in args.withIndex()) {
//        println(" $index = $arg")
//    }

    val opponents = listOf(
        Warrior(name = "Goblin", health = 60, attackPower = 10, stamina = 5, defensePower = 2),
        Warrior(name = "Orc", health = 100, attackPower = 20, stamina = 10, defensePower = 5),
        Warrior(name = "Troll", health = 200, attackPower = 20, stamina = 10, defensePower = 0),
        Sorcerer(name = "Wizard", health = 60, attackPower = 8, mana = 15, healingPower = 5),
        Sorcerer(name = "Warlock", health = 100, attackPower = 10, mana = 10, healingPower = 2),
    )

    println("\nEnter your character name:")
    val characterName = requireNotNull(readLine()) { "Invalid character name" }

    println("\nEnter your character class (W|S):")
    val characterClass = requireNotNull(readLine()) { "Invalid character class" }

    val character = when (characterClass) {
        "W" -> Warrior(characterName, 100, 10, 5, 1)
        "S" -> Sorcerer(characterName, 50, 5, 20, 5)
        else -> throw IllegalArgumentException("Invalid character class")
    }

    val opponent = opponents.random()

    println("\n$characterName is ready for battle!")
    println("\nTheir opponent is ${opponent.name}!")

    match(character, opponent)
}

//fun main(args: Array<String>) {
//    println("Received ${args.size} arguments:")
//
//    for ((index, arg) in args.withIndex()) {
//        println(" $index = $arg")
//    }
//
//    println("\nEnter your name:")
//    val name = readLine()
//    println("\nHello, $name!")
//}
