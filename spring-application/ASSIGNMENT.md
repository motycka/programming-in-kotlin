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
```

### Requirements

---
## Matches API
``` 
GET /api/matches
POST /api/matches
```

### Model
```
```

### Requirements

---
## Leaderboard API
```
GET /api/leaderboard?class=(WARRIOR|SORCERER)
```
### Model
```
```

### Requirements

---
## User Management API
They already have user management available, they can register new user ...
```
POST /api/accounts
```
### Model
```
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
