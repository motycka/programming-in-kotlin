package com.motycka.edu

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import kotlin.test.assertEquals

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class HelloRepositoryJpaTest {

    @Autowired
    private lateinit var helloRepository: HelloRepositoryJpa

    @Test
    fun `should select message`() {
        val result = helloRepository.findByLocale("en")
        assertEquals(
            HelloEntity(
                id = 1,
                locale = "en",
                hello = "Hello"
            ),
            result
        )
    }

    @Test
    fun `should insert message`() {
        val message = HelloEntity(
            locale = "it",
            hello = "Ciao"
        )
        helloRepository.save(message)
        val result = helloRepository.findByLocale("it")
        assertEquals(message, result)
    }
}
