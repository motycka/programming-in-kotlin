package com.motycka.edu

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/greetings")
class HelloController(
    private val helloService: HelloService
) {

    @GetMapping
    fun getHello(
        @RequestParam(value = "name") name: String,
        @RequestParam(value = "locale") locale: String
    ): String {
        return helloService.sayHello(name, locale)
    }

    @GetMapping("/en")
    fun getHelloEn(
        @RequestParam(value = "name") name: String,
    ): String {
        return helloService.sayHello(name, "en")
    }

    @PostMapping
    fun saveHello(): ResponseEntity<String?> {
        return ResponseEntity.badRequest().body("Sorry, not implemented")
    }

}
