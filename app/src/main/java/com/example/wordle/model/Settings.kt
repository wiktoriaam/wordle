package com.example.wordle.model

import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object Settings{
    const val GUESSES_NR = 6
    const val WORD_LENGTH = 5
    val KEYBOARD_LAYOUT = listOf(
        "QWERTYUIOP",
        "ASDFGHJKL",
        "ZXCVBNM"
    )

    @OptIn(ExperimentalUuidApi::class)
    val uuid: UUID = UUID.fromString("d7ad8670-977f-4cdb-9904-67810c94ade3")
}