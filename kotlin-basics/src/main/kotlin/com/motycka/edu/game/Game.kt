package com.motycka.edu.game

fun main() {
    val results = Game(
        matchService = RandomMatchService(
            characterService = RandomCharacterService()
        )
    ).playMatch(20)

//    GameFactory.createGame().playMatch(20)
}


internal object GameFactory {
    fun createGame(): Game {
        val characterService = RandomCharacterService()
        val matchService = RandomMatchService(characterService)
        return Game(matchService)
    }
}

internal class Game(
    val matchService: MatchService
) {
    fun playMatch(rounds: Int): MatchResult {
        return matchService.match(rounds)
    }
}

internal interface CharacterService {
    fun findChallenger(strategy: CharacterMatchingStrategy? = null): Character
    fun findOpponent(challenger: Character, strategy: CharacterMatchingStrategy? = null): Character
}

internal data class CharacterMatchingStrategy(
    val name: String?,
    val level: CharacterLevel?,
    val characterClass: String?
) {
    companion object {
        val ANY = CharacterMatchingStrategy(null, null, null)
    }
}

internal class RandomCharacterService(
    private val characterRepository: CharacterRepository = CharacterRepository
): CharacterService {
    override fun findChallenger(strategy: CharacterMatchingStrategy?): Character {
        return characterRepository.getCharacters().random()
    }

    override fun findOpponent(challenger: Character, strategy: CharacterMatchingStrategy?): Character {
        return characterRepository.getCharacters().filterNot(challenger::equals).random()
    }
}

internal interface MatchService {
    fun match(rounds: Int, matchingStrategy: CharacterMatchingStrategy? = null): MatchResult
}

internal class RandomMatchService(
    val characterService: CharacterService,
): MatchService {
    override fun match(rounds: Int, matchingStrategy: CharacterMatchingStrategy?): MatchResult {
        val challenger = characterService.findChallenger(matchingStrategy)
        return Match(
            rounds = rounds,
            challenger = challenger,
            opponent = characterService.findOpponent(challenger, matchingStrategy)
        ).fight()
    }
}

internal object CharacterRepository {

    private val harryPotterCharacters = listOf(
        Sorcerer(name = "Harry Potter", health = 100, attackPower = 40, mana = 30, healingPower = 30, level = CharacterLevel.LEVEL_1), // Balanced, more durable
        Sorcerer(name = "Hermione Granger", health = 90, attackPower = 40, mana = 40, healingPower = 30, level = CharacterLevel.LEVEL_1), // High mana for tactical advantage
        Sorcerer(name = "Ron Weasley", health = 120, attackPower = 50, mana = 20, healingPower = 10, level = CharacterLevel.LEVEL_1), // Higher health for tanking
        Sorcerer(name = "Severus Snape", health = 80, attackPower = 60, mana = 30, healingPower = 30, level = CharacterLevel.LEVEL_1), // Strong attack, slightly higher health
        Sorcerer(name = "Albus Dumbledore", health = 90, attackPower = 40, mana = 40, healingPower = 30, level = CharacterLevel.LEVEL_1), // Well-rounded, high healing power
        Sorcerer(name = "Lord Voldemort", health = 80, attackPower = 80, mana = 10, healingPower = 30, level = CharacterLevel.LEVEL_1), // Powerful, but needs more survival
        Sorcerer(name = "Minerva McGonagall", health = 100, attackPower = 40, mana = 30, healingPower = 30, level = CharacterLevel.LEVEL_1), // Balanced
        Sorcerer(name = "Bellatrix Lestrange", health = 80, attackPower = 70, mana = 20, healingPower = 30, level = CharacterLevel.LEVEL_1), // Aggressive, but not overpowered
        Sorcerer(name = "Draco Malfoy", health = 100, attackPower = 40, mana = 30, healingPower = 30, level = CharacterLevel.LEVEL_1), // Slightly more balanced
        Sorcerer(name = "Neville Longbottom", health = 130, attackPower = 30, mana = 10, healingPower = 30, level = CharacterLevel.LEVEL_1), // Tanky with healing ability
    )

    private val starWarsCharacters = listOf(
        Warrior(name = "Luke Skywalker", health = 110, attackPower = 40, stamina = 20, defensePower = 30, level = CharacterLevel.LEVEL_1), // Balanced, well-rounded
        Warrior(name = "Yoda", health = 80, attackPower = 30, stamina = 50, defensePower = 40, level = CharacterLevel.LEVEL_1), // High mana, strategic healer
        Warrior(name = "Han Solo", health = 120, attackPower = 40, stamina = 10, defensePower = 30, level = CharacterLevel.LEVEL_1), // Tanky and reliable
        Warrior(name = "Darth Vader", health = 100, attackPower = 60, stamina = 10, defensePower = 30, level = CharacterLevel.LEVEL_1), // High health and power, needs healing
        Warrior(name = "Obi-Wan Kenobi", health = 100, attackPower = 40, stamina = 30, defensePower = 30, level = CharacterLevel.LEVEL_1), // Strong defense and healing
        Warrior(name = "Emperor Palpatine", health = 80, attackPower = 80, stamina = 10, defensePower = 30, level = CharacterLevel.LEVEL_1), // Strong attack, low healing
        Warrior(name = "Mace Windu", health = 110, attackPower = 40, stamina = 20, defensePower = 30, level = CharacterLevel.LEVEL_1), // Well-rounded with decent attack
        Warrior(name = "Darth Maul", health = 90, attackPower = 60, stamina = 20, defensePower = 30, level = CharacterLevel.LEVEL_1), // Fast and aggressive, needs support
        Warrior(name = "Kylo Ren", health = 100, attackPower = 50, stamina = 20, defensePower = 30, level = CharacterLevel.LEVEL_1), // Balanced with raw power
        Warrior(name = "Finn", health = 130, attackPower = 20, stamina = 10, defensePower = 40, level = CharacterLevel.LEVEL_1), // High health and resilience, strategic healing
    )

    fun getCharacters(): List<Character> {
        return harryPotterCharacters + starWarsCharacters
    }

}
