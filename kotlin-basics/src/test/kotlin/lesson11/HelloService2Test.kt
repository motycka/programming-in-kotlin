//package lesson11
//
//import io.kotest.core.spec.style.FunSpec
//import io.kotest.matchers.shouldBe
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.context.TestConfiguration
//import org.springframework.context.annotation.Bean
//
//
//@SpringBootTest
//class HelloService2Test(
//    private val helloRepository: HelloRepository,
//    private val helloService: HelloService
//) : FunSpec() {
//
//    @TestConfiguration
//    class ServiceTestConfig {
//        @Bean
//        fun helloRepository() = mockk<HelloRepository>()
//    }
//
//    init {
//        test("should return localized hello") {
//            every { helloRepository.selectHelloInLanguage(any()) } returns "Hello"
//
//            val language = "en"
//
//            helloService.sayHello("Monika", language).shouldBe("Hello Monika!")
//
//            verify(exactly = 1) { helloRepository.selectHelloInLanguage(language) }
//        }
//    }
//
//}
