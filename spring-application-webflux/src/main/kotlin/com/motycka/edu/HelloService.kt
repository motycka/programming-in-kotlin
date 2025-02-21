package com.motycka.edu

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class HelloService(
    private val helloRepository: HelloRepositoryJooq,
    private val eventPublisher: ApplicationEventPublisher
) {
    fun getHello(): List<Hello> {
        return helloRepository.selectAll().map { record ->
            Hello(
                locale = record.locale,
                hello = record.hello
            )
        }
    }

    fun sayHello(name: String, locale: String): String {
        val hello = helloRepository.selectHello(locale)
            ?.hello
            ?: error("Hello not found for locale $locale")
        eventPublisher.publishEvent(HelloEvent(hello))
        return hello
    }

    fun getHelloAsync(): Flow<List<Hello>> = flow {
        val helloList = helloRepository.selectAll().map { record ->
            Hello(
                locale = record.locale,
                hello = record.hello
            )
        }

        delay(3000)

        emit(helloList)
    }

    suspend fun sayHelloAsync(name: String, locale: String): Flow<String> = flow {
        val hello = helloRepository.selectHello(locale)
            ?.hello
            ?: error("Hello not found for locale $locale")

        delay(3000)

        eventPublisher.publishEvent(HelloEvent(hello))

        emit("$hello $name")
    }
}

data class Hello(
    val locale: String,
    val hello: String
)
