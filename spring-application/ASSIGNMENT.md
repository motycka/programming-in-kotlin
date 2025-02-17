I need to write down assignment for my students. The task is to write Spring Boot application in Kotlin.

It is a fantasy game simulation where players can create characters and fight with each other.

It should implement the following REST interface

## Characters API
```
GET /api/characters
GET /api/characters/{id}
POST /api/characters
GET /api/characters/challengers
GET /api/characters/opponents
PUT /api/characters/{id}
```

### Model
```
enum class CharacterClass {
    WARRIOR,
    SORCERER,
}

data class CharacterCreateRequest(
    val name: String,
    val health: Int,
    val attackPower: Int,
    val stamina: Int? = null,
    val defensePower: Int? = null,
    val mana: Int? = null,
    val healingPower: Int? = null,
    val characterClass: CharacterClass
)

data class CharacterResponse(
    val id: CharacterId,
    val name: String,
    val health: Int,
    val attackPower: Int,
    val stamina: Int?,
    val defensePower: Int?,
    val mana: Int?,
    val healingPower: Int?,
    val characterClass: CharacterClass,
    val level: CharacterLevel,
    val experience: Int,
    val shouldLevelUp: Boolean,
    val isOwner: Boolean
)

data class CharacterUpdateRequest(
    val id: CharacterId,
    val name: String,
    val health: Int,
    val attackPower: Int,
    val stamina: Int?,
    val defensePower: Int?,
    val mana: Int?,
    val healingPower: Int?
)
```

### Requirements
- Character should have health, attack power, level and experience
- Warrior should have stamina and defense power
- Sorcerer should have mana and healing power
- The service should allow to create a new character. It should validate that points are distributed correctly.
- The service should allow to update character attributes (level up), it should validate that points are distributed correctly.
- The service should allow to get all characters, get character by id, get all challengers and get all opponents.
- Challengers = characters owned by current user (the one whi is logged in)
- Opponents = characters not owned by current user

---
## Matches API
``` 
GET /api/matches
POST /api/matches
```

### Model
```
data class MatchCharacterResponse(
    val id: CharacterId,
    val name: String,
    val characterClass: CharacterClass,
    val level: CharacterLevel,
    val experienceTotal: Int,
    val experienceGained: Int,
    val isVictor: Boolean
)

data class MatchRequest(
    val rounds: Int,
    val challengerId: CharacterId,
    val opponentId: CharacterId
)

data class MatchResponse(
    val id: MatchId,
    val challenger: MatchCharacterResponse,
    val opponent: MatchCharacterResponse,
    val rounds: List<MatchRoundResponse>,
)

data class MatchRoundResponse(
    val round: Int,
    val characterId: CharacterId,
    val healthDelta: Int,
    val staminaDelta: Int,
    val manaDelta: Int
)
```

### Requirements
- The service should allow to create a new match (POST). 
- It should validate that characters are valid and that the user is the owner of the challenger character.
- The service should allow to get all matches.
- The match should return the list of rounds with the changes in health, stamina and mana for each character.
- The match should also update character statistics (experience, wins, loses, draws) based on the outcome of the match.

---
## Leaderboard API
```
GET /api/leaderboards?class=(WARRIOR|SORCERER|null)
```
### Model
```
data class LeaderboardResponse(
    val position: Int,
    val character: CharacterResponse,
    val wins: Int,
    val losses: Int,
    val draws: Int
)

```
### Requirements
- The service should allow to get the leaderboard sorted by position (how I leave up to you)
- The leaderboard should allow filtering by class (WARRIOR, SORCERER) or no filter at all.

---
## User Management API
They already have user management available, they can register new user ...
```
POST /api/accounts
```
### Model
```
data class AccountRegistrationRequest(
    val name: String,
    val username: String,
    val password: String
)
```

Authentication is done using Basic Auth. 
The user is authenticated using the username and password.


There is a simple UI available at /index.html where they can interact with the API, 
if the API is written according to the specification.

---
## Database

Database should be in-memory H2 database. 
Basic model is provided, but they are free to update it.

They can use other database if they want.


## General requirements

You need to be authenticated with your username and password to use the API. I have already provided you with the user management API.
You just need to register your own user (or add it to the database init script).

### Testing
(describe testing reqs, use jUnit and Mockk)

## Project Structure
(describe structure of the project here)

## Final Recommendations
(describe Recommendations)
