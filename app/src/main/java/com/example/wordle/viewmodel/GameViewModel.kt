package com.example.wordle.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wordle.model.LetterState
import com.example.wordle.model.Settings
import com.example.wordle.model.WordleGame
import com.example.wordle.ui.theme.WordleDarkGray
import com.example.wordle.ui.theme.WordleGreen
import com.example.wordle.ui.theme.WordleLightGray
import com.example.wordle.ui.theme.WordleYellow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(
    val wordleGame: WordleGame = WordleGame()
) : ViewModel(){
//    private val gameScreenState = MutableStateFlow(GameScreenState(
//        currentLetterIndex = 0,
//        currentGuessIndex = wordleGame.getGuessNr(),
//        letterColors = ('A'..'Z').associateWith { WordleLightGray } as MutableMap<Char, Color>,
//        inputBoxColors = MutableList(Settings.GUESSES_NR){
//            MutableList(Settings.WORD_LENGTH){Pair(' ', WordleLightGray)}
//        },
//        errorMessage = ""
//    ))
    var currentWord: String = ""
    private var currentIndex = 0
    private var currentGuess = 0
    private val _letterColors = MutableStateFlow(('A'..'Z').associateWith { WordleLightGray })
    private val _inputBoxColors = MutableStateFlow(List(Settings.GUESSES_NR){
        List(Settings.WORD_LENGTH){Pair(' ', WordleLightGray)}
    })
    private val _errorMessage = MutableStateFlow("")

    val letterColors: StateFlow<Map<Char, Color>> = _letterColors
    val inputBoxColors: StateFlow<List<List<Pair<Char,Color>>>> = _inputBoxColors
    val errorMessage: StateFlow<String> = _errorMessage
//    val screenState: StateFlow<GameScreenState> = gameScreenState.asStateFlow()

    private var gameFinished: Boolean = false

    fun onEnterClick(){
        if(gameFinished)return

        if(currentIndex < Settings.WORD_LENGTH){
            _errorMessage.value = "Word is too short!"
        }
        else if(!wordleGame.isWordValid(currentWord)){
            _errorMessage.value = "Word not found!"
        }
        else{
            val letterStates = wordleGame.validateGuess(currentWord)

            for(idx in letterStates.indices){
                var newColor = when(letterStates[idx].second){
                    LetterState.CORRECT -> WordleGreen
                    LetterState.INCORRECT -> WordleDarkGray
                    LetterState.UNCHECKED -> WordleLightGray
                    LetterState.IN_WORD -> WordleYellow
                }
                if(wordleGame.isGameWon())newColor = WordleGreen
                val newRow = _inputBoxColors.value[currentGuess].toMutableList().apply { this[idx] = Pair(currentWord[idx], newColor) }
                val newInput = _inputBoxColors.value.toMutableList().apply { this[currentGuess] = newRow }
                _inputBoxColors.value = newInput
                if(newColor != WordleYellow){
                    val newInput = _letterColors.value.toMutableMap().apply { this[currentWord[idx]] = newColor }
                    _letterColors.value = newInput
                }
                else if(_letterColors.value[currentWord[idx]] != WordleGreen){
                    val newInput = _letterColors.value.toMutableMap().apply { this[currentWord[idx]] = newColor }
                    _letterColors.value = newInput
                }
                if(wordleGame.isGameWon() || wordleGame.isGameOver()){
                    gameFinished = true
                }
            }
            currentGuess += 1
            currentIndex = 0
            currentWord = ""

        }
    }


    fun onBackSpaceClick(){
        _errorMessage.value = ""
        if(gameFinished || currentIndex == 0)return
        currentWord = currentWord.dropLast(1)
        val newRow = _inputBoxColors.value[currentGuess].toMutableList().apply { this[currentIndex - 1] = Pair(' ', WordleLightGray) }
        val newInput = _inputBoxColors.value.toMutableList().apply { this[currentGuess] = newRow }
        _inputBoxColors.value = newInput
        currentIndex -= 1
    }

    fun onKeyClick(
        c: Char
    ){
        _errorMessage.value = ""
        if(gameFinished || currentIndex >= Settings.WORD_LENGTH)return
        currentWord += c
        val newRow = _inputBoxColors.value[currentGuess].toMutableList().apply { this[currentIndex] = Pair(c, WordleLightGray) }
        val newInput = _inputBoxColors.value.toMutableList().apply { this[currentGuess] = newRow }
        _inputBoxColors.value = newInput
        currentIndex += 1
    }



}