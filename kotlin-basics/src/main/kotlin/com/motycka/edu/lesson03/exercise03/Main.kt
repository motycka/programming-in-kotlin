package com.motycka.edu.lesson03.exercise03

fun main() {

    val person = Person(name = "Moni", contact = Contact())

    // mutating the object
    with(person.contact) {
        email = "monika.protivova@gmail.com"
        phone = "+420 123 456 789"
    }

    // creating a new object
    val newPerson = person.let { person ->
        person.copy(
            contact = person.contact.copy(
                email = "monika.protivova@gmail.com",
                phone = "+420 987 654 321")
        )
    }

    println("Person: $person")
    println("New person: $newPerson")

}

data class Person(
    val name: String,
    val contact: Contact
)

data class Contact(
    var email: String? = null,
    var phone: String? = null
)
