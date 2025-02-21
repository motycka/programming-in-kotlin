package com.motycka.edu

import com.fasterxml.jackson.databind.ObjectMapper
import com.motycka.edu.HelloController
import com.motycka.edu.HelloService
import com.motycka.edu.SecurityConfiguration
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HelloController::class)
@Import(SecurityConfiguration::class)
class HelloControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var helloService: HelloService

    @BeforeEach
    fun mocks() {
        every { helloService.sayHello("Kotlin", "en") } returns "Hello Kotlin"
        every { helloService.sayHello("Kotlin", "cs") } returns "Ahoj Kotlin"
    }

    @Test
    fun `get hello in locale cs`() {
        mockMvc.perform(get("/api/hello?name=Kotlin&locale=cs")
            .contentType("application/json")
            .with(httpBasic("username", "password")))
            .andExpect(status().isOk)

        verify {
            helloService.sayHello("Kotlin", "cs")
        }
    }

    @Test
    fun `should get hello in it`() {
        mockMvc.perform(post("/api/hello")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(HelloRequest(locale = "it", hello = "Ciao")))
            .with(httpBasic("username", "password")))
            .andExpect(status().isNotFound)
    }

}
