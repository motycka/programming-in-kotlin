package com.motycka.edu

import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test

class HelloServiceTest {

    private val helloRepository: HelloRepositoryJdbc = mockk()
    private val helloService = HelloService(
        helloRepository = helloRepository
    )

    @Test
    fun `should get hello in locale cs`() {

        every {
            helloRepository.selectMessage("cs", "hello")
        } returns Greeting(
            locale = "cs",
            messageKey = "hello",
            messageValue = "Ahoj"
        )

        val result = helloService.sayHello("Kotlin", "cs")

        assert(result == "Ahoj Kotlin")

    }
}
