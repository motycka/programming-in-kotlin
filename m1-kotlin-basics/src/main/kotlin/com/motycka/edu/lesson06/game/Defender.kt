package com.motycka.edu.lesson06.game

internal interface Defender {
    val name: String
    var stamina: Int
    val defensePower: Int
    fun defend(attackPower: Int): Int
}
