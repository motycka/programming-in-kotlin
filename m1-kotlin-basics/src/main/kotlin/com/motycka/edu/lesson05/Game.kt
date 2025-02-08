package com.motycka.edu.lesson05

// TODO
// add one mana and one stamina every round

fun main() {
    match(
        character1 = Warrior(
            name = "Thorne Ironfist",
            health = 100,
            attackPower = 10,
            stamina = 10,
            defensePower = 5
        ),
        character2 = Sorcerer(
            name = "Eldrin Starfire",
            health = 50,
            attackPower = 20,
            mana = 5,
            healingPower = 10
        )
    )
}

fun match(character1: Character, character2: Character) {
    var round = 0
    while (character1.health > 0 && character2.health > 0 && round < 10) {
        round++
        println("\nROUND $round:")

        character1.beforeRound()
        character2.beforeRound()

        character1.attack(character2)
        character2.attack(character1)

        character1.afterRound()
        character2.afterRound()

        println("${character1.name} has ${character1.health} health remaining")
        println("${character2.name} has ${character2.health} health remaining")
    }

    when {
        character1.health <= 0 && character2.health > 0 -> println("\n${character2.name} is the victor in round $round!")
        character2.health <= 0 && character1.health > 0 -> println("\n${character2.name} is the victor in round $round!")
        else -> println("\nIt's a draw!")
    }
}

interface Healer {
    var mana: Int
    val healingPower: Int
    fun heal()
}

interface Defender {
    val name: String
    var stamina: Int
    val defensePower: Int
    fun defend(attackPower: Int): Int
}

interface Recoverable {
    // TODO
    fun beforeRound()

    // TODO
    fun afterRound()
}

abstract class Character(
    val name: String,
    var health: Int,
    val attackPower: Int
) : Recoverable {
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

class Warrior(
    name: String,
    health: Int,
    attackPower: Int,
    override var stamina: Int,
    override val defensePower: Int,
) : Character(
    name = name,
    health = health,
    attackPower = attackPower
), Defender {

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
            stamina--
            attackPower - defensePower
        } else {
            println("$name is too tired to defend")
            attackPower
        }
    }

    override fun beforeRound() {
        stamina++
        println("$name recovers 1 stamina")
    }

    override fun afterRound() {
        // nothing
    }
}

class Sorcerer(
    name: String,
    health: Int,
    attackPower: Int,
    override var mana: Int,
    override val healingPower: Int
) : Character(
    name = name,
    health = health,
    attackPower = attackPower,
), Healer, Recoverable {

    private val maxHealth = health

    override fun attack(target: Character) {
        heal()
        when {
            health <= 0 -> println("$name is dead and cannot attack")
            mana <= 0 -> println("$name out of mana")
            else -> {
                println("$name casts a spell at ${target.name}")
                target.health -= attackPower
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
                mana--
            }
        }
    }

    override fun beforeRound() {
        mana++
        println("$name recovers 1 mana")
    }

    override fun afterRound() {
        // nothing
    }
}
