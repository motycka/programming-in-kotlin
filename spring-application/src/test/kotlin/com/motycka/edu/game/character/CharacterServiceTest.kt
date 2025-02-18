package com.motycka.edu.game.character

import com.motycka.edu.game.account.AccountService
import com.motycka.edu.game.character.model.CharacterLevel
import com.motycka.edu.game.character.model.Warrior
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CharacterServiceTest {

    private lateinit var characterRepository: CharacterRepository
    private lateinit var accountService: AccountService
    private lateinit var characterService: CharacterService

    private val accountId = 1L

    @BeforeEach
    fun setUp() {
        characterRepository = mockk()
        accountService = mockk()
        characterService = CharacterService(characterRepository, accountService)
    }

    @Test
    fun `createCharacter should return created character`() {
        val character = Warrior(
            id = 1,
            accountId = accountId,
            name = "Warrior",
            health = 140,
            attackPower = 20,
            experience = 0,
            stamina = 20,
            defensePower = 20,
            level = CharacterLevel.LEVEL_1
        )

        every { accountService.getCurrentAccountId() } returns accountId
        every { characterRepository.insertCharacters(accountId = accountId, character = character) } returns character

        val result = characterService.createCharacter(character)

        assertEquals(character, result)
        verify { characterRepository.insertCharacters(accountId = accountId, character = character) }
    }

//    @Test
//    fun `getCharacters should return list of characters`() {
//        val accountId = 1L
//        val filter = CharactersFilter.DEFAULT
//        val characters = listOf(mockk<Character>())
//
//        every { accountService.getCurrentAccountId() } returns accountId
//        every { characterRepository.selectWithFilter(accountId, filter) } returns characters
//
//        val result = characterService.getCharacters(filter)
//
//        assertEquals(characters, result)
//        verify { characterRepository.selectWithFilter(accountId, filter) }
//    }
}
