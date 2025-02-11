//package lesson11
//
//import io.kotest.core.spec.style.FunSpec
//import io.mockk.every
//import io.mockk.mockk
//import org.hamcrest.Matchers.containsString
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.boot.test.context.TestConfiguration
//import org.springframework.context.annotation.Bean
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//
//@WebMvcTest(HelloController::class)
//class HelloControllerTest(
//    private val helloService: HelloService,
//    private val mockMvc: MockMvc
//) : FunSpec() {
//
//    @TestConfiguration
//    class ServiceTestConfig {
//        @Bean
//        fun helloService() = mockk<HelloService>()
//    }
//
//    private val path = "/api/hello"
//    private val name = "Monika"
//    private val locale = "es"
//    private val expected = "Hola Monika!"
//
//    init {
//        beforeEach {
//            every { helloService.sayHello(name, locale) } returns expected
//        }
//
//        test("GET /api/hello?name={name}&locale={locale} - should responds with 200") {
//            mockMvc.perform(get("$path?name=$name&locale=$locale"))
//                .andDo(print())
//                .andExpect(status().isOk)
//                .andExpect(content().string(containsString(expected)))
//        }
//
//        test("GET /api/hello - should responds with 400") {
//            mockMvc.perform(get(path))
//                .andDo(print())
//                .andExpect(status().isBadRequest)
//        }
//    }
//
//}
//
