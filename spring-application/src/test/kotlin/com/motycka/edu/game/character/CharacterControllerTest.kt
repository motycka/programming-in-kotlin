package com.motycka.edu.game.character

import com.motycka.edu.game.character.rest.CharacterCreateRequest
import com.motycka.edu.game.character.rest.CharacterResponse
import com.motycka.edu.game.character.rest.CharacterUpdateRequest
import com.motycka.edu.game.character.rest.CharactersFilter
import com.motycka.edu.game.user.AccountService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(CharacterController::class)
class CharacterControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var characterService: CharacterService

    @MockBean
    private lateinit var accountService: AccountService

    private lateinit var characterResponse: CharacterResponse

    @BeforeEach
    fun setUp() {
        characterResponse = mockk()
    }

    @Test
    fun `postCharacter should return created character`() {
        val characterCreateRequest = mockk<CharacterCreateRequest>()
        val accountId = 1L

        every { accountService.getCurrentAccountId() } returns accountId
        every { characterService.createCharacter(any()) } returns characterResponse

        mockMvc.perform(post("/api/characters")
            .contentType("application/json")
            .content("""{"name":"Test Character"}"""))
            .andExpect(status().isOk)
            .andExpect(content().json("""{"id":null,"name":"Test Character"}"""))

        verify { characterService.createCharacter(any()) }
    }

    @Test
    fun `getCharacters should return list of characters`() {
        val accountId = 1L
        val characters = listOf(characterResponse)

        every { accountService.getCurrentAccountId() } returns accountId
        every { characterService.getCharacters(CharactersFilter.DEFAULT) } returns characters

        mockMvc.perform(get("/api/characters"))
            .andExpect(status().isOk)
            .andExpect(content().json("[]"))

        verify { characterService.getCharacters(CharactersFilter.DEFAULT) }
    }

    @Test
    fun `putCharacter should return updated character`() {
        val characterUpdateRequest = mockk<CharacterUpdateRequest>()
        val characterId = 1L

        every { characterService.getCharacter(characterId) } returns characterResponse
        every { characterService.updateCharacter(any()) } returns characterResponse

        mockMvc.perform(put("/api/characters/$characterId")
            .contentType("application/json")
            .content("""{"name":"Updated Character"}"""))
            .andExpect(status().isOk)
            .andExpect(content().json("""{"id":null,"name":"Updated Character"}"""))

        verify { characterService.updateCharacter(any()) }
    }
}
