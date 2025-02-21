package com.motycka.edu

import com.motycka.edu.HelloTableTable.HELLO_FIELD
import com.motycka.edu.HelloTableTable.HELLO_TABLE
import com.motycka.edu.HelloTableTable.HelloRecord
import com.motycka.edu.HelloTableTable.ID_FIELD
import com.motycka.edu.HelloTableTable.LOCALE_FIELD
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class HelloRepositoryJooq(
    private val dsl: DSLContext
) {

    fun selectAll(): List<HelloRecord> {
        return dsl
            .select(
                ID_FIELD,
                LOCALE_FIELD,
                HELLO_FIELD
            )
            .from(HELLO_TABLE)
            .fetch()
            .mapNotNull(HelloTableTable::mapRecord)
    }

    fun selectHello(locale: String): HelloRecord? {
        return dsl
            .select(
                ID_FIELD,
                LOCALE_FIELD,
                HELLO_FIELD
            )
            .from(HELLO_TABLE)
            .where(
                LOCALE_FIELD.eq(locale)
            )
            .fetchOne(HelloTableTable::mapRecord)
    }

    fun insertHello(message: HelloRecord) {
        dsl.insertInto(HELLO_TABLE)
            .set(LOCALE_FIELD, message.locale)
            .set(HELLO_FIELD, message.hello)
            .execute()
    }

}


object HelloTableTable {
    val HELLO_TABLE = DSL.table("hello")
    val ID_FIELD = DSL.field("id", Long::class.java)
    val LOCALE_FIELD = DSL.field("locale", String::class.java)
    val HELLO_FIELD = DSL.field("hello", String::class.java)

    data class HelloRecord(
        val id: Long? = null,
        val locale: String,
        val hello: String
    )

    fun mapRecord(record: Record): HelloRecord {
        return HelloRecord(
            id = record.get("id", Long::class.java),
            locale = record.get("locale", String::class.java),
            hello = record.get("hello", String::class.java)
        )
    }
}

