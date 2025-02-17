package com.motycka.edu.game.leaderboard

import com.motycka.edu.game.character.CharacterService
import com.motycka.edu.game.character.rest.CharacterId
import com.motycka.edu.game.character.rest.CharacterResponse
import com.motycka.edu.game.character.rest.CharactersFilter
import com.motycka.edu.game.character.rest.toCharacterResponse
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/scores")
class LeaderboardController(
    private val leaderboardService: LeaderboardService
) {

    @GetMapping
    fun getLeaderboard(): List<LeaderboardTo> {
        return leaderboardService.getLeaderboard()
    }

}

@Service
class LeaderboardService(
    private val leaderboardRepository: LeaderboardRepository,
    private val characterService: CharacterService
) {

    fun getLeaderboard(): List<LeaderboardTo> {
        val characters = characterService.getCharacters(CharactersFilter.DEFAULT)
        return characters.mapIndexed { index, character ->
            LeaderboardTo(
                position = 1,
                character = LeaderboardCharacterTo(
                    character = character.toCharacterResponse(),
                    wins = 0,
                    losses = 0,
                    draws = 0
                )
            )
        }
    }
}

@Repository
class LeaderboardRepository(
    private val jdbcTemplate: JdbcTemplate
) {

    fun selectLeaderboard(): List<LeaderboardTo> {
        return emptyList()
    }
}

data class LeaderboardCharacter(
    val character: CharacterId,
    val wins: Long,
    val losses: Long,
    val draws: Long
)

data class LeaderboardTo(
    val position: Long,
    val character: LeaderboardCharacterTo
)

data class LeaderboardCharacterTo(
    val character: CharacterResponse,
    val wins: Long,
    val losses: Long,
    val draws: Long
)
