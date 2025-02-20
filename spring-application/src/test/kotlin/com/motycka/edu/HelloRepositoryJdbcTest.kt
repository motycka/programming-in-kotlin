package com.motycka.edu

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import kotlin.test.assertEquals

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = [HelloRepositoryJdbc::class])
class HelloRepositoryJdbcTest {

    @Autowired
    private lateinit var helloRepository: HelloRepositoryJdbc

    @Test
    fun `should select message`() {
        val result = helloRepository.selectMessage("en", "hello")
        assertEquals(
            Greeting(
                locale = "en",
                messageKey = "hello",
                messageValue = "Hello"
            ),
            result
        )
    }

    @Test
    fun `should insert message`() {
        val greeting = Greeting(
            locale = "it",
            messageKey = "hello",
            messageValue = "Ciao"
        )
        helloRepository.insertMessage(greeting)
        val result = helloRepository.selectMessage(greeting.locale, greeting.messageKey)
//        assertEquals(message.messageValue, result)
    }
}
