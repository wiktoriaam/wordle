package com.example.wordle.model


import kotlin.random.Random


/*
A class designed to handle the core logic of wordle, check a new word etc.
Knows nothing about the UI
 */

class WordleGame (
    val wordList: List<String> = WordsReader.getWordList()
){
    private var gameState = GameState(
        secretWord = "WORDS",
        guessCount = 0,
        gameStatus = GameStatus.INPROGRESS,
        guesses = MutableList(Settings.GUESSES_NR){
            MutableList(Settings.WORD_LENGTH){Pair(' ', LetterState.UNCHECKED)}
        },
        secretLetters = emptyList(),
        letterStates = ('A'..'Z').associateWith { LetterState.UNCHECKED } as MutableMap<Char, LetterState>
    )


    init {
        val newWordIndex = Random.nextInt(0, wordList.size)
        gameState = gameState.copy(
            secretWord = wordList[newWordIndex],
            secretLetters = wordList[newWordIndex].toList()
        )
    }

    fun isGameOver() : Boolean{
        return (gameState.guessCount == Settings.GUESSES_NR)
    }

    fun isGameWon() : Boolean {
        return (gameState.gameStatus == GameStatus.WIN)
    }

    fun isWordValid(word: String) : Boolean {
        return (wordList.contains(word))
    }

    fun validateGuess(word: String) : List<Pair<Char, LetterState>> {
        var incorrectLetterFound = false
        for(c in word.indices){
            var letterState = LetterState.INCORRECT
            if(word[c] == gameState.secretWord[c]){
                letterState = LetterState.CORRECT
            }
            else if(word[c] in gameState.secretLetters){
                letterState = LetterState.IN_WORD
                incorrectLetterFound = true
            }
            else{
                incorrectLetterFound = true
            }

            //changing the letter status in the guessed word
            gameState.guesses[gameState.guessCount][c] = Pair(word[c], letterState)

            //changing the letter status(in general)
            if(!(letterState == LetterState.IN_WORD && gameState.letterStates[word[c]] == LetterState.CORRECT)){
                gameState.letterStates[word[c]] = letterState
            }
        }
        gameState.guessCount += 1

        if(!incorrectLetterFound){
            gameState.gameStatus = GameStatus.WIN
        }
        else if(gameState.guessCount >= Settings.GUESSES_NR){
            gameState.gameStatus = GameStatus.LOSE
        }

        return gameState.guesses[gameState.guessCount-1]
    }
    fun revealWord(): String{
        return gameState.secretWord
    }
}