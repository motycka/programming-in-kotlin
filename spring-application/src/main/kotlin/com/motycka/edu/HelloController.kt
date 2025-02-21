package com.motycka.edu

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/hello")
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


    @PostMapping
    fun postHello(
        @RequestBody body: HelloRequest
    ): ResponseEntity<Unit> {
        helloService.insertHello(
            locale = body.locale,
            hello = body.hello
        )
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

}

data class HelloRequest(
    val locale: String,
    val hello: String
)
