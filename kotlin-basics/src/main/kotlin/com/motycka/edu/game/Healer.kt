package com.motycka.edu.game

internal interface Healer {
    val mana: Int
    val healingPower: Int
    fun heal()
}
