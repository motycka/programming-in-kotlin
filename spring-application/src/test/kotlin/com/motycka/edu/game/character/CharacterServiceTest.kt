package com.motycka.edu.game.character

import com.motycka.edu.game.character.model.Character
import com.motycka.edu.game.character.rest.CharactersFilter
import com.motycka.edu.game.user.AccountService
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

    @BeforeEach
    fun setUp() {
        characterRepository = mockk()
        accountService = mockk()
        characterService = CharacterService(characterRepository, accountService)
    }

    @Test
    fun `createCharacter should return created character`() {
        val character = mockk<Character>()
        val accountId = 1L

        every { accountService.getCurrentAccountId() } returns accountId
        every { characterRepository.insertCharacters(accountId, character) } returns character

        val result = characterService.createCharacter(character)

        assertEquals(character, result)
        verify { characterRepository.insertCharacters(accountId, character) }
    }

    @Test
    fun `getCharacters should return list of characters`() {
        val accountId = 1L
        val filter = CharactersFilter.DEFAULT
        val characters = listOf(mockk<Character>())

        every { accountService.getCurrentAccountId() } returns accountId
        every { characterRepository.selectWithFilter(accountId, filter) } returns characters

        val result = characterService.getCharacters(filter)

        assertEquals(characters, result)
        verify { characterRepository.selectWithFilter(accountId, filter) }
    }
}
