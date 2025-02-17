package com.motycka.edu.game.character.rest

import com.motycka.edu.game.character.AccountCharacter
import com.motycka.edu.game.character.model.Character
import com.motycka.edu.game.character.model.CharacterLevel
import com.motycka.edu.game.character.model.Sorcerer
import com.motycka.edu.game.character.model.Warrior
import com.motycka.edu.game.user.model.AccountId

fun List<AccountCharacter>.toCharacterResponses() = map { it.toCharacterResponse() }

fun AccountCharacter.toCharacterResponse(): CharacterResponse {
    return character.toCharacterResponse()
}

fun Character.toCharacterResponse(): CharacterResponse {
    return when (this) {
        is Sorcerer -> toCharacterResponse()
        is Warrior -> toCharacterResponse()
        else -> {
            throw error("Unknown character class")
        }
    }
}

fun Sorcerer.toCharacterResponse(currentAccountId: AccountId) = CharacterResponse(
    id = requireNotNull(id) { "Character id must not be null." },
    name = name,
    health = health,
    attackPower = attackPower,
    stamina = null,
    defensePower = null,
    mana = mana,
    healingPower = healingPower,
    characterClass = CharacterClass.SORCERER,
    level = level,
    experience = experience,
    shouldLevelUp = getPoints() < level.points,
    thisAccount = true //accountId == currentAccountId
)

fun Warrior.toCharacterResponse() = CharacterResponse(
    id = requireNotNull(id) { "Character id must not be null." },
    name = name,
    health = health,
    attackPower = attackPower,
    stamina = stamina,
    defensePower = defensePower,
    mana = null,
    healingPower = null,
    characterClass = CharacterClass.WARRIOR,
    level = level,
    experience = experience,
    shouldLevelUp = getPoints() < level.points,
    thisAccount = false
)

fun CharacterCreateRequest.toCharacter(): Character {
    return when (characterClass) {
        CharacterClass.WARRIOR -> toWarrior()
        CharacterClass.SORCERER -> toSorcerer()
    }
}

private fun CharacterCreateRequest.toSorcerer() = Sorcerer(
    id = null,
    name = name,
    health = health,
    attackPower = attackPower,
    mana = requireNotNull(mana) { "Mana must not be null." },
    healingPower = requireNotNull(healingPower) { "Mana must not be null." },
    level = CharacterLevel.LEVEL_1,
    experience = 0
)

private fun CharacterCreateRequest.toWarrior() = Warrior(
    id = null,
    name = name,
    health = health,
    attackPower = attackPower,
    stamina = requireNotNull(stamina) { "Stamina must not be null." },
    defensePower = requireNotNull(defensePower) { "Defense power must not be null." },
    level = CharacterLevel.LEVEL_1,
    experience = 0
)

fun CharacterUpdateRequest.toCharacter(id: CharacterId, existing: Character): Character {
    return when (existing) {
        is Warrior -> toWarrior(id, existing)
        is Sorcerer -> toSorcerer(id, existing)
        else -> error("TODO") // TODO
    }
}

fun CharacterUpdateRequest.toSorcerer(id: CharacterId, existing: Character) = Sorcerer(
    id = id,
    name = name,
    health = health,
    attackPower = attackPower,
    mana = requireNotNull(mana) { "Mana must not be null." },
    healingPower = requireNotNull(healingPower) { "Mana must not be null." },
    level = existing.level,
    experience = existing.experience
)

fun CharacterUpdateRequest.toWarrior(id: CharacterId, existing: Character) = Warrior(
    id = id,
    name = name,
    health = health,
    attackPower = attackPower,
    stamina = requireNotNull(stamina) { "Stamina must not be null." },
    defensePower = requireNotNull(defensePower) { "Defense power must not be null." },
    level = existing.level,
    experience = existing.experience
)
