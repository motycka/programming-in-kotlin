package com.motycka.edu.lesson05.interfaces

/*
Update the Character, Warrior and Sorcerer classes from the previous exercise, instead of open class make in abstract class.

Create new interfaces Defender with the following methods and properties:
- val name: String
- var stamina: Int
- val defensePower: Int
- fun defend(attackPower: Int): Int

Have the Warrior class implement the Defender interface:
- Implement the defend function to reduce the attackPower by the defensePower and decrease the stamina by 1
- If the warrior has stamina <= 0, print "$name is too tired to defend" and return the attackPower
- If the warrior has stamina > 0, print "$name raises shield and defends against $defensePower damage" and return attackPower - defensePower
- Override the receiveAttack function to call the defend function and then call the super.receiveAttack function with the result
    override fun receiveAttack(attackPower: Int) {
        super.receiveAttack(defend(health - attackPower))
    }

Create new interfaces Healer with the following methods and properties:
- var mana: Int
- val healingPower: Int
- fun heal()

Have the Sorcerer class implement the Healer interface:
- Implement the heal function to increase the health by the healingPower and decrease the mana by 1
- If the sorcerer has mana <= 0, print "$name is out of mana" and do not heal
- If the sorcerer has mana > 0, print "$name heals self to $health health" and increase the health by healingPower
- Update the attack function to call heal function before attacking


Update the test code to use the new classes and properties and run the match function.
 */
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

internal interface Healer {
    var mana: Int
    val healingPower: Int
    fun heal()
}

internal interface Defender {
    val name: String
    var stamina: Int
    val defensePower: Int
    fun defend(attackPower: Int): Int
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
}

internal class Sorcerer(
    name: String,
    health: Int,
    attackPower: Int,
    override var mana: Int,
    override val healingPower: Int
) : Character(
    name = name,
    health = health,
    attackPower = attackPower,
), Healer {

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
}
