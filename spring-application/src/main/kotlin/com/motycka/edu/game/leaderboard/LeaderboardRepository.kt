package com.motycka.edu.game.leaderboard

import com.motycka.edu.game.character.rest.CharacterId
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class LeaderboardRepository(
    private val jdbcTemplate: JdbcTemplate
) {

    fun selectLeaderboard(): List<Leaderboard> {
        return jdbcTemplate.query(
            "SELECT * FROM leaderboard ORDER BY wins - losses DESC",
            ::rowMapper
        )
    }

    fun updateLeaderboard(characterId: CharacterId, win: Boolean, loss: Boolean) {
        jdbcTemplate.update(
            "UPDATE leaderboard SET wins = wins + ?, losses = losses + ? WHERE character_id = ?",
            if (win) 1 else 0,
            if (loss) 1 else 0,
            characterId
        )
    }

    private fun rowMapper(rs: ResultSet, index: Int): Leaderboard {
        return Leaderboard(
            position = index,
            characterId = rs.getLong("character_id"),
            wins = rs.getInt("wins"),
            losses = rs.getInt("losses"),
            draws = rs.getInt("draws")
        )
    }

}
