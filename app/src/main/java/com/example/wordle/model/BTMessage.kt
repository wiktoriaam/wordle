package com.example.wordle.model

class BTMessage(
    val type: Int = 0,
    val round: Int = 0
) {
    //val type: Int = 0//0 - round, 1 - won, 2 - lost, 3-disconnected
    //val round: Int = 0//which round, if won, how many rounds it took
}