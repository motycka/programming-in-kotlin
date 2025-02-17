package com.motycka.edu.game.match

import com.motycka.edu.game.match.rest.MatchResponse
import com.motycka.edu.game.match.rest.MatchRequest
import com.motycka.edu.game.match.rest.toMatchResultTo
import com.motycka.edu.game.match.rest.toMatchResultTos
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/matches")
class MatchController(
    private val matchService: MatchService,
) {

    @GetMapping
    fun getMatches(): List<MatchResponse> {
        return matchService.getMatches().toMatchResultTos()
    }

    @PostMapping
    fun postMatch(
        @RequestBody newMatchTo: MatchRequest
    ): MatchResponse {
        return matchService.doMatch(
            rounds = newMatchTo.rounds,
            challengerId = newMatchTo.challengerId,
            opponentId = newMatchTo.opponentId
        ).toMatchResultTo()
    }
}


