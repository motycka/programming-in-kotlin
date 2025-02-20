package com.motycka.edu

import com.motycka.edu.HelloRepositoryJpa
import com.motycka.edu.MessageEntity
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.assertEquals

@DataJpaTest
class HelloRepositoryJpaTest {

    @Autowired
    lateinit var helloRepository: HelloRepositoryJpa

    @Test
    fun `should select message`() {
        val result = helloRepository.findByLocaleAndMessageKey("en", "hello")
        assertEquals(
            MessageEntity(
                id = 1,
                locale = "en",
                messageKey = "hello",
                messageValue = "Hello"
            ),
            result
        )
    }

    @Test
    fun `should insert message`() {
        val message = MessageEntity(
            locale = "it",
            messageKey = "hello",
            messageValue = "Ciao"
        )
        helloRepository.save(message)
        val result = helloRepository.findByLocaleAndMessageKey("it", "hello")
        assertEquals(message, result)
    }
}
