package com.example.wordle.model

data class GameState (
    val secretWord: String,
    val secretLetters: List<Char>,
    var guessCount: Int,
    var gameStatus: GameStatus,
    var guesses: MutableList<MutableList<Pair<Char, LetterState>>>,
    var letterStates: MutableMap<Char, LetterState>
)