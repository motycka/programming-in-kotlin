package com.motycka.edu

import org.springframework.stereotype.Service

@Service
class HelloService(
    private val helloRepository: HelloRepositoryJdbc
) {
    fun sayHello(name: String, locale: String): String {
        val key = HelloRepositoryJdbc.HELLO_MESSAGE_KEY
        val hello = helloRepository.selectMessage(locale, key)
            ?.messageValue
            ?: error("No message found for locale $locale and key $key")
        return "$hello $name"
    }
}
