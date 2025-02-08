package com.motycka.edu.lesson03.exercise02

fun main() {

    val numbers = arrayOf(0, 1, 10)
    val strings = arrayOf("a", "b", "c")

    for (number in numbers) {
        println("\nNumber $number:")
        println(" even? ${number.isEven()}")
        println(" odd? ${number.isOdd()}")
    }

    println("\nRepeating strings: ${strings.joinToString()}")
    println(" ${strings.repeat().joinToString()}")
}

fun Int.isEven(): Boolean = this % 2 == 0

fun Int.isOdd(): Boolean = isEven().not()

fun Array<String>.repeat(): Array<String> = this + this
