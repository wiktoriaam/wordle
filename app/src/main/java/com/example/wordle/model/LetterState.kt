package com.example.wordle.model

enum class LetterState {
    UNCHECKED, //not checked in the current game
    IN_WORD, //present in the word, but in the wrong place
    CORRECT, //present in the word in this exact place
    INCORRECT //not present in the guessed word
}