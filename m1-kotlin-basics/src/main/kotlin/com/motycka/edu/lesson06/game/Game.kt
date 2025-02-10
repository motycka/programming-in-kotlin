package com.motycka.edu.lesson06.game

fun main() {

    val match = Match(
        rounds = 20,
        challenger = Warrior(
            name = "Thorne Ironfist",
            health = 160,
            attackPower = 24,
            stamina = 14,
            defensePower = 2,
            level = CharacterLevel.LEVEL_1
        ),
        opponent = Sorcerer(
            name = "Eldrin Starfire",
            health = 150,
            attackPower = 24,
            mana = 24,
            healingPower = 2,
            level = CharacterLevel.LEVEL_1
        )
    )

    match.fight()
}
