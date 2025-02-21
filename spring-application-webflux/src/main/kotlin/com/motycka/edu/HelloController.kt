package com.motycka.edu

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/hello")
class HelloControllerV1(
    private val helloService: HelloService
) {


    @GetMapping
    fun getHello(): Flux<Hello> = Flux.fromIterable(helloService.getHello())

    @GetMapping("/{locale}")
    fun getHello(
        @PathVariable(value = "locale") locale: String,
        @RequestParam(value = "name") name: String
    ): Mono<String> = Mono.just(helloService.sayHello(name, locale))

}


@RestController
@RequestMapping("/api/v2/hello")
class HelloControllerV2(
    private val helloService: HelloService
) {

    @GetMapping
    suspend fun getHello(): Flow<List<Hello>> = helloService.getHelloAsync()

    @GetMapping("/{locale}")
    suspend fun getHello(
        @PathVariable(value = "locale") locale: String,
        @RequestParam(value = "name") name: String
    ): Flow<String> = helloService.sayHelloAsync(name, locale)

}
