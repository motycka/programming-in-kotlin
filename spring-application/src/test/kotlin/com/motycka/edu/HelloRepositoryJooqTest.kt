//package com.motycka.edu
//
//import com.motycka.edu.GreetingTable.GreetingRecord
//import org.jooq.DSLContext
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.jooq.JooqTest
//import kotlin.test.assertEquals
//
//@JooqTest
//class HelloRepositoryJooqTest {
//
//    @Autowired
//    private lateinit var helloRepository: HelloRepositoryJooq
//
//    @Autowired
//    private lateinit var dsl: DSLContext
//
//    @Test
//    fun `should select message`() {
//        val result = helloRepository.selectMessage("en", "hello")
//        assertEquals(
//            GreetingRecord(
//                locale = "en",
//                messageKey = "hello",
//                messageValue = "Hello"
//            ),
//            result
//        )
//    }
//
//    @Test
//    fun `should insert message`() {
//        val message = GreetingRecord(
//            locale = "it",
//            messageKey = "hello",
//            messageValue = "Ciao"
//        )
//        helloRepository.insertMessage(message)
//        val result = helloRepository.selectMessage("it", "hello")
//        assertEquals(message, result)
//    }
//}
