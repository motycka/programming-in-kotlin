package com.motycka.edu.tracker.user

import com.motycka.edu.tracker.user.model.NewUser
import com.motycka.edu.tracker.user.model.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.getUsers())
    }

    @GetMapping("{id}")
    fun getUserById(@PathVariable("id") id: Long): ResponseEntity<User> {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    fun createUser(@RequestBody user: NewUser): ResponseEntity<User> {
        return ResponseEntity(userService.createUser(user), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    fun updateUser(
        @PathVariable("id") id: Long,
        @RequestBody user: User
    ): ResponseEntity<User> {
        return ResponseEntity.ok(userService.updateUser(user.copyWithId(id)));
    }

    @DeleteMapping("{id}")
    fun deleteUser(@PathVariable("id") id: Long): ResponseEntity<Object> {
        userService.deleteUser(id)
        return ResponseEntity.ok().build()
    }
}
