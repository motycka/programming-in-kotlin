//package com.motycka.edu
//
//import com.motycka.edu.GreetingTable.GREETING
//import com.motycka.edu.GreetingTable.GreetingRecord
//import com.motycka.edu.GreetingTable.LOCALE
//import com.motycka.edu.GreetingTable.MESSAGE_KEY
//import com.motycka.edu.GreetingTable.MESSAGE_VALUE
//import org.jooq.DSLContext
//import org.jooq.impl.DSL
//import org.jooq.Record
//import org.springframework.stereotype.Repository
//
//@Repository
//class HelloRepositoryJooq(
//    private val dsl: DSLContext
//) {
//
//    fun selectMessage(locale: String, key: String): Greeting? {
//        return dsl.selectFrom(GREETING)
//            .where(LOCALE.eq(locale).and(MESSAGE_KEY.eq(key)))
//            .fetchOne { record ->
//                GreetingRecord(
//                    id = record.get("id", Long::class.java),
//                    locale = record.get("locale", String::class.java),
//                    messageKey = record.get("message_key", String::class.java),
//                    messageValue = record.get("message_value", String::class.java)
//                )
//            }?.let {
//                Greeting(
//                    locale = it.locale,
//                    messageKey = it.messageKey,
//                    messageValue = it.messageValue
//                )
//            }
//    }
//
//    fun insertMessage(message: GreetingRecord) {
//        dsl.insertInto(GREETING)
//            .set(LOCALE, message.locale)
//            .set(MESSAGE_KEY, message.messageKey)
//            .set(MESSAGE_VALUE, message.messageValue)
//            .execute()
//    }
//}
//
//
//object GreetingTable {
//    val greeting = "greeting"
//    val id = "id"
//    val locale = "locale"
//    val messageKey = "message_key"
//    val messageValue = "message_value"
//
//    val GREETING = DSL.table(greeting)
//    val ID = DSL.field(id, Long::class.java)
//    val LOCALE = DSL.field(locale, String::class.java)
//    val MESSAGE_KEY = DSL.field(messageKey, String::class.java)
//    val MESSAGE_VALUE = DSL.field(messageValue, String::class.java)
//
//    data class GreetingRecord(
//        val id: Long? = null,
//        val locale: String,
//        val messageKey: String,
//        val messageValue: String
//    ) {
//        fun Record.map() {
//            GreetingRecord(
//                id = get(GreetingTable.id, Long::class.java),
//                locale = get(GreetingTable.locale, String::class.java),
//                messageKey = get(GreetingTable.messageKey, String::class.java),
//                messageValue = get(GreetingTable.messageValue, String::class.java)
//            )
//        }
//    }
//}
//
