package com.example.wordle.viewmodel

import androidx.compose.ui.graphics.Color

data class GameScreenState(
    var currentLetterIndex: Int,
    var currentGuessIndex: Int,
    var letterColors: MutableMap<Char, Color>,
    var inputBoxColors: MutableList<MutableList<Pair<Char, Color>>>,
    var errorMessage: String
    )