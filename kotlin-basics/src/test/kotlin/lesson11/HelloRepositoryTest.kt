//package lesson11
//
//import io.kotest.core.spec.style.FunSpec
//import org.junit.jupiter.api.Assertions
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
//import org.springframework.context.annotation.Import
//import org.springframework.jdbc.core.JdbcTemplate
//
//@JdbcTest
//@Import(HelloRepository::class, JdbcTemplate::class)
//class HelloRepositoryTest(
////    private val jdbcTemplate: JdbcTemplate,
////    private val helloRepository: HelloRepository
//) : FunSpec() {
//
//    @Autowired
//    private lateinit var jdbcTemplate: JdbcTemplate
//
//    @Autowired
//    private lateinit var helloRepository: HelloRepository
//
//    init {
//        test("Should return 'Hello' in English") {
//            val result = helloRepository.selectHelloInLanguage("en")
//            Assertions.assertEquals("Hello", result)
//        }
//    }
//
//}
