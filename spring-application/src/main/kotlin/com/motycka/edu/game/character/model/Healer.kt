package com.motycka.edu.game.character.model

internal interface Healer {
    var mana: Int
    val healingPower: Int
    fun heal()
}
