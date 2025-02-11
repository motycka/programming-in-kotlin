package com.motycka.edu.lesson05.generics

import kotlin.math.PI

// TODO not matching assignment

class Box<T>(
    private var item: T? = null
) {

    fun putItem(item: T) {
        this.item = item
    }

    fun getItem(): T? {
        return item
    }
}

fun <T1, T2, R> insertIntoBox(item1: T1, item2: T2, func: (T1, T2) -> R): R {
    return func(item1, item2)
}


interface Drawer<T1, T2, R> {
    fun insert(item1: T1, item2: T2): R
}

class SmallDrawer: Drawer<Int, String, String> {
    override fun insert(item1: Int, item2: String): String {
        return "Drawer has $item1 $item2"
    }
}

fun main() {
    val box1 = Box<String>()
    box1.putItem("Apple")
    println("Box 1 has ${box1.getItem()}")

    val box2 = Box<Double>()
    box2.putItem(PI)
    println("Box 2 has ${box2.getItem()}")

    val result = insertIntoBox(3, "Bananas") { item1, item2 -> "Box has $item1 $item2" }
    println(result)

    val drawer = SmallDrawer()
    println(drawer.insert(5, "Nails"))
}
