package com.motycka.edu

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HelloService(
    private val helloRepository: HelloRepositoryJdbc
) {
    fun sayHello(name: String, locale: String): String {
        val hello = helloRepository.selectHello(locale)
            ?.hello
            ?: error("Hello not found for locale $locale")
        return "$hello $name"
    }

    @Transactional
    fun insertHello(locale: String, hello: String) {
        helloRepository.insertHello(
            Hello(
                locale = locale,
                hello = hello
            )
        )
    }

}

data class Hello(
    val locale: String,
    val hello: String
)
