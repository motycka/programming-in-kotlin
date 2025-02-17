package com.motycka.edu.game.character

import com.motycka.edu.game.character.model.Character
import com.motycka.edu.game.character.rest.CharacterId
import com.motycka.edu.game.character.rest.CharactersFilter
import com.motycka.edu.game.error.NotFoundException
import com.motycka.edu.game.user.AccountService
import com.motycka.edu.game.user.model.AccountId
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}


data class AccountCharacter(
    val accountId: AccountId,
    val character: Character
)

@Service
class CharacterService(
    private val characterRepository: CharacterRepository,
    private val accountService: AccountService,
) {

    fun createCharacter(character: Character): AccountCharacter {
        val accountId = accountService.getCurrentAccountId()

        val newCharacter = characterRepository.insertCharacters(
            accountId = accountService.getCurrentAccountId(),
            character = character
        ) ?: error(CREATE_ERROR)

        return AccountCharacter(
            accountId = accountId,
            character = newCharacter
        )
    }

    fun getCharacters(filter: CharactersFilter): List<AccountCharacter> {
        val accountId = accountService.getCurrentAccountId()

        return characterRepository.selectWithFilter(accountId, filter).map {
            AccountCharacter(
                accountId = accountId,
                character = it
            )
        }
    }

    fun getCharacter(characterId: CharacterId): AccountCharacter {
        return getCharacters(
            CharactersFilter(
                ids = setOf(characterId),
                includeChallengers = true,
                includeOpponents = true
            )
        ).firstOrNull() ?: throw NotFoundException()
    }

    fun updateCharacter(character: Character): AccountCharacter {
        val accountId = accountService.getCurrentAccountId()
        val updatedCharacter = characterRepository.updateCharacter(character) ?: error(UPDATE_ERROR)
        return AccountCharacter(
            accountId = accountId,
            character = updatedCharacter
        )
    }

    companion object {
        const val CREATE_ERROR = "Character could not be created."
        const val UPDATE_ERROR = "Character could not be updated."
    }

}
