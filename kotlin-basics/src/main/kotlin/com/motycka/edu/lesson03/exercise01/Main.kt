package com.motycka.edu.lesson03.exercise01

fun main() {

    val input = arrayOf("a", "b", "c", "d", "e")

    val output = updateAtIndexV2(input, 1, 3) { it.uppercase() }

    output.forEach(::println)

}

fun updateAtIndexV1(array: Array<String>, vararg atIndex: Int, func: (String) -> String): Array<String> {
    val newArray = array.copyOf()
    for ((index, _) in array.withIndex()) {
        if (index in atIndex) newArray[index] = func(newArray[index])
    }
    return newArray
}

fun updateAtIndexV2(array: Array<String>, vararg atIndex: Int, func: (String) -> String): Array<String> {
    return array.mapIndexed { index, element ->
        if (index in atIndex) func(element) else element
    }.toTypedArray()
}


