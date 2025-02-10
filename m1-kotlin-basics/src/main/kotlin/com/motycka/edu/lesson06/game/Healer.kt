package com.motycka.edu.lesson06.game

internal interface Healer {
    var mana: Int
    val healingPower: Int
    fun heal()
}
