package com.example.wordle.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordle.data.local.AppDatabase
import com.example.wordle.data.local.entities.GameDetails
import com.example.wordle.data.local.entities.GameRecord
import com.example.wordle.model.LetterState
import com.example.wordle.model.Settings
import com.example.wordle.model.WordleGame
import com.example.wordle.ui.theme.WordleDarkGray
import com.example.wordle.ui.theme.WordleGreen
import com.example.wordle.ui.theme.WordleLightGray
import com.example.wordle.ui.theme.WordleYellow
import com.example.wordle.utils.AudioManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    application: Application
) : AndroidViewModel(application){
    // wordleGame and database objects
    private val wordleGame: WordleGame = WordleGame()
    private val db = AppDatabase.getInstance(application)
    private val dao = db.gameHistoryDao()

    /*
    variables to store the state of current game not available in wordleGame, typical for viewModel
    */
    var currentWord: String = ""
    private var currentIndex = 0
    private var currentGuess = 0
    private val _letterColors = MutableStateFlow(('A'..'Z').associateWith { WordleLightGray })
    private val _inputBoxColors = MutableStateFlow(List(Settings.GUESSES_NR){
        List(Settings.WORD_LENGTH){Pair(' ', WordleLightGray)}
    })
    private val _errorMessage = MutableStateFlow("")
    private val _secretWord = MutableStateFlow<String?>(null)
    private var _gameFinished = MutableStateFlow(false)
    private var _gameResult = MutableStateFlow(false)

    /*
    * variables to be observed by GameScreen
    */
    val letterColors: StateFlow<Map<Char, Color>> = _letterColors
    val inputBoxColors: StateFlow<List<List<Pair<Char,Color>>>> = _inputBoxColors
    val errorMessage: StateFlow<String> = _errorMessage
    val secretWord: StateFlow<String?> = _secretWord

    val gameFinished: StateFlow<Boolean>  = _gameFinished
    val gameResult: StateFlow<Boolean> = _gameResult


    fun onEnterClick(){
        if(_gameFinished.value)return

        AudioManager.playClickSound()

        if(currentIndex < Settings.WORD_LENGTH){
            _errorMessage.value = "Word is too short!"
        }
        else if(!wordleGame.isWordValid(currentWord)){
            _errorMessage.value = "Word not found!"
        }
        else{
            val letterStates = wordleGame.validateGuess(currentWord)

            for(idx in letterStates.indices){
                val newColor = when(letterStates[idx].second){
                    LetterState.CORRECT -> WordleGreen
                    LetterState.INCORRECT -> WordleDarkGray
                    LetterState.UNCHECKED -> WordleLightGray
                    LetterState.IN_WORD -> WordleYellow
                }
                val newRow = _inputBoxColors.value[currentGuess].toMutableList().apply { this[idx] = Pair(currentWord[idx], newColor) }
                val newInputBoxColors = _inputBoxColors.value.toMutableList().apply { this[currentGuess] = newRow }
                _inputBoxColors.value = newInputBoxColors
                if(newColor != WordleYellow){
                    val newLetterColors = _letterColors.value.toMutableMap().apply { this[currentWord[idx]] = newColor }
                    _letterColors.value = newLetterColors
                }
                else if(_letterColors.value[currentWord[idx]] != WordleGreen) {
                    val newLetterColors = _letterColors.value.toMutableMap()
                        .apply { this[currentWord[idx]] = newColor }
                    _letterColors.value = newLetterColors
                }
            }
            if(wordleGame.isGameWon() || wordleGame.isGameOver()){
                _secretWord.value = wordleGame.revealWord()
                insertGame(currentGuess+1, wordleGame.isGameWon(), _secretWord.value!!)
                if(wordleGame.isGameWon())_gameResult.value = true
                _gameFinished.value = true
            }
            currentGuess += 1
            currentIndex = 0
            currentWord = ""

        }
    }


    fun onBackSpaceClick(){
        AudioManager.playClickSound()
        _errorMessage.value = ""
        if(_gameFinished.value || currentIndex == 0)return
        currentWord = currentWord.dropLast(1)
        val newRow = _inputBoxColors.value[currentGuess].toMutableList().apply { this[currentIndex - 1] = Pair(' ', WordleLightGray) }
        val newInput = _inputBoxColors.value.toMutableList().apply { this[currentGuess] = newRow }
        _inputBoxColors.value = newInput
        currentIndex -= 1
    }

    fun onKeyClick(
        c: Char
    ){
        AudioManager.playClickSound()
        _errorMessage.value = ""
        if(_gameFinished.value || currentIndex >= Settings.WORD_LENGTH)return
        currentWord += c
        val newRow = _inputBoxColors.value[currentGuess].toMutableList().apply { this[currentIndex] = Pair(c, WordleLightGray) }
        val newInput = _inputBoxColors.value.toMutableList().apply { this[currentGuess] = newRow }
        _inputBoxColors.value = newInput
        currentIndex += 1
    }

    fun insertGame(
        guessNr: Int,
        gameResult: Boolean,
        secretWord: String
    ){
        viewModelScope.launch{
            val gameHistory = GameRecord(
                tries = guessNr,
                secretWord = secretWord,
                gameResult = gameResult,
                multiplayer = false
            )
            val gameId = dao.insertGame(gameHistory)
            for (i in 1..guessNr){
                val gameDetail = GameDetails(
                    gameId = gameId,
                    guessedWord = _inputBoxColors.value[i-1].map{pair -> pair.first}.joinToString(""),
                    status = _inputBoxColors.value[i-1].map{pair -> when(pair.second){
                        WordleGreen -> 0
                        WordleDarkGray -> 1
                        WordleLightGray -> 2
                        WordleYellow -> 3
                        else -> 2
                    }.toString()}.joinToString(""),
                    order = i
                )
                dao.insertGameDetails(gameDetail)
            }
        }
    }

}