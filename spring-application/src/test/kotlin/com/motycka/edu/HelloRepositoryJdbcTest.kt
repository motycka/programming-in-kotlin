package com.motycka.edu

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = [HelloRepositoryJdbc::class])
class HelloRepositoryJdbcTest {

    @Autowired
    private lateinit var helloRepository: HelloRepositoryJdbc

    @Test
    fun `should select message`() {
        val result = helloRepository.selectHello("en")

        Assertions.assertEquals("en", result!!.locale)
        Assertions.assertEquals("Hello", result.hello)
    }

    @Test
    fun `should insert message`() {
        val hello = Hello(
            locale = "it",
            hello = "Ciao"
        )
        helloRepository.insertHello(hello)
        val result = helloRepository.selectHello(hello.locale)
        Assertions.assertEquals(hello, result)
    }
}
