package com.motycka.edu.game.leaderboard

import com.motycka.edu.game.leaderboard.rest.LeaderboardResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/scores")
class LeaderboardController(
    private val leaderboardService: LeaderboardService
) {

    @GetMapping
    fun getLeaderboard(): List<LeaderboardResponse> {
        return leaderboardService.getLeaderboard()
    }

}
