//package lesson11
//
//import io.kotest.core.spec.style.FunSpec
//import io.kotest.matchers.shouldBe
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//
//class HelloServiceTest: FunSpec({
//
//    val helloRepository: HelloRepository = mockk()
//    val helloService = HelloService(helloRepository)
//
//    test("should return localized hello") {
//        every { helloRepository.selectHelloInLanguage(any()) } returns "Hello"
//
//        val language = "en"
//
//        helloService.sayHello("Monika", language).shouldBe("Hello Monika!")
//
//        verify(exactly = 1) { helloRepository.selectHelloInLanguage(language) }
//    }
//
//})
