package com.example.wordle.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.wordle.data.local.AppDatabase
import com.example.wordle.data.local.entities.GameDetails
import com.example.wordle.data.local.entities.GameRecord
import com.example.wordle.model.BTMessage
import com.example.wordle.model.LetterState
import com.example.wordle.model.Settings
import com.example.wordle.model.WordleGame
import com.example.wordle.ui.theme.WordleDarkGray
import com.example.wordle.ui.theme.WordleGreen
import com.example.wordle.ui.theme.WordleLightGray
import com.example.wordle.ui.theme.WordleYellow
import com.example.wordle.utils.AudioManager
import com.example.wordle.utils.BTManager
import com.example.wordle.utils.SpeechRecognitionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class GameViewModel(
    application: Application,
    val btManager: BTManager
) : AndroidViewModel(application){
    private val wordleGame: WordleGame = WordleGame()
    private val db = AppDatabase.getInstance(application)
    private val dao = db.gameHistoryDao()

    // variables for speech recognition. listening is a boolean indicating whether the keyboard is active(listening=false) or the input is taken from microphone
    var recognizedText by mutableStateOf("")
        private set
    var listening by mutableStateOf(false)
        private set
    private val speechRecognitionManager = SpeechRecognitionManager(application)

    var currentWord: String = ""
    private var currentIndex = 0

    private val _letterColors = MutableStateFlow(('A'..'Z').associateWith { WordleLightGray })
    private val _errorMessage = MutableStateFlow("")

    private var insertedToDatabase = false

    val letterColors: StateFlow<Map<Char, Color>> = _letterColors
    val inputBoxColors: StateFlow<List<List<Pair<Char,Color>>>> = btManager.inputBoxColors
    val errorMessage: StateFlow<String> = _errorMessage
    val secretWord: StateFlow<String?> = btManager.secretWord

    val gameFinished: StateFlow<Boolean>  = btManager.gameFinished
    val gameResult: StateFlow<Boolean> = btManager.gameResult
    var multiplayer : StateFlow<Boolean> = btManager.gameStarted
    var opponentRound: StateFlow<Int> = btManager.opponentRound
    var opponentStatus: StateFlow<Int> = btManager.opponentStatus
    var myGameFinished: StateFlow<Boolean> = btManager.myGameFinished
    val finishedBeforeOpponent: StateFlow<Boolean> = btManager.finishedBeforeOpponent



    fun onEnterClick(){
        if(listening) return
        if(myGameFinished.value)return

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
                btManager.updateInput(currentWord,newColor, idx)
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
                btManager.onMyGameFinished()
                btManager.revealSecretWord(wordleGame.revealWord())
                if(wordleGame.isGameWon()){
                    sendToOpponent(BTMessage(1,btManager.currentGuess+1))
                }
                else{
                    sendToOpponent(BTMessage(2,6))
                }
                if(wordleGame.isGameWon()){
                    btManager.guessed()
                }
                if(multiplayer.value) {
                    if (opponentStatus.value == 0) btManager.finishedBefore()
                    else {
                        btManager.setGameResult(decideOnWinner())
                        viewModelScope.launch{
                            insertGame(btManager.currentGuess + 1, gameResult.value, secretWord.value!!)
                        }
                        btManager.onGameFinished()
                    }
                }
                if(!multiplayer.value){
                    btManager.onGameFinished()
                    btManager.setGameResult(decideOnWinner())
                    viewModelScope.launch{
                        insertGame(btManager.currentGuess + 1, gameResult.value, secretWord.value!!)
                    }
                }
                return
            }
            btManager.updateGuess()
            currentIndex = 0
            currentWord = ""
            sendToOpponent(BTMessage(0,btManager.currentGuess+1))
        }
    }


    fun onBackSpaceClick(){
        if(listening) return
        AudioManager.playClickSound()
        _errorMessage.value = ""
        if(myGameFinished.value || currentIndex == 0)return
        currentWord = currentWord.dropLast(1)
        btManager.updateLetter(' ', WordleLightGray, currentIndex-1)
        currentIndex -= 1
    }

    fun onKeyClick(
        c: Char
    ){
        if(listening) return
        AudioManager.playClickSound()
        _errorMessage.value = ""
        if(myGameFinished.value || currentIndex >= Settings.WORD_LENGTH)return
        currentWord += c
        btManager.updateLetter(c, WordleLightGray, currentIndex)
        currentIndex += 1
    }

    suspend fun insertGame(
        guessNr: Int,
        gameResult: Boolean,
        secretWord: String
    ){
            val gameHistory = GameRecord(
                tries = guessNr,
                guessed = btManager.myWordGuessed,
                secretWord = secretWord,
                gameResult = gameResult,
                multiplayer = multiplayer.value,
                opponentTries = opponentRound.value,
                opponentGuessed = (opponentStatus.value == 1)
            )
            val gameId = dao.insertGame(gameHistory)
            for (i in 1..guessNr){
                val gameDetail = GameDetails(
                    gameId = gameId,
                    guessedWord = inputBoxColors.value[i-1].map{pair -> pair.first}.joinToString(""),
                    status = inputBoxColors.value[i-1].map{pair -> when(pair.second){
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
            insertedToDatabase = true
    }

    fun updateRecognizedText(
        data: String
    ){
        recognizedText = data
    }

    fun onMicrophoneClick(
        requestPermission:  () -> Unit
    ){
        AudioManager.playClickSound()
        if(myGameFinished.value)return
        _errorMessage.value = ""
        val permissionGranted = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED


        if(permissionGranted){
            listening = true
            speechRecognitionManager.startListening(
                onRes = { data -> onSpeechRecognitionResult(data)},
                onError = { error -> onSpeechRecognitionError(error)}
            )
        }
        else{
            requestPermission()
        }
    }

    fun onSpeechRecognitionResult(
        data: String
    ){
        recognizedText = data.uppercase()
        listening = false
        if(recognizedText.length == 5) {
            currentWord = recognizedText
            for(idx in currentWord.indices){
                btManager.updateInput(currentWord, WordleLightGray, idx)
            }
            currentIndex = 5
        }
        else{
            _errorMessage.value = "Word must have 5 letters. Try again!"
        }
    }

    fun onSpeechRecognitionError(
        error: Int
    ){
        _errorMessage.value = when(error){
            SpeechRecognizer.ERROR_AUDIO -> "Speech Recognition: audio error!"
            SpeechRecognizer.ERROR_CLIENT -> "Speech Recognition: error on client's side"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permission error"
            SpeechRecognizer.ERROR_NETWORK -> "Speech Recognition: No internet!"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Speech Recognition: timeout!"
            SpeechRecognizer.ERROR_NO_MATCH -> "Speech Recognition error: try again!"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Speech Recognition: service busy!"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech Recognition: timeout"
            else -> "Speech Recognition error: try again!"
        }
        listening = false
    }

    fun listenToOpponent(){
        viewModelScope.launch(Dispatchers.IO){
            var numBytes: Int = -1
            var bytes: ByteArray = ByteArray(2)
            val inputStream = btManager.socket!!.inputStream
            while(true){
                    try {
                        numBytes = inputStream.read(bytes)
                    } catch (e: IOException) {
                        opponentDisconnected()
                        break
                    }

                if(numBytes < 0){
                    opponentDisconnected()
                    break
                }

                val newRound = bytes[1].toInt()
                val newStatus = bytes[0].toInt()

                btManager.updateStatus(newStatus)
                btManager.updateRound(newRound)

                Log.d(
                    "received",
                    opponentStatus.value.toString() + opponentRound.value.toString()
                )
                if (myGameFinished.value && (opponentStatus.value != 0)) {
                    btManager.setGameResult(decideOnWinner())
                    insertGame(btManager.currentGuess+1, gameResult.value, secretWord.value!!)
                    btManager.onGameFinished()

                    break
                }

            }

        }

    }


    fun decideOnWinner(): Boolean{
        //assumes both games ended, that needs to be checked earlier
        if (btManager.myWordGuessed && opponentStatus.value == 1) {
            //both guessed correctly
            if (btManager.currentGuess + 1 == opponentRound.value) return finishedBeforeOpponent.value
            else if (btManager.currentGuess + 1 < opponentRound.value) return true

        }
        else if(btManager.myWordGuessed){
            return true
        }
        return false
    }

    fun sendToOpponent(
        message: BTMessage
    ){
        if(!multiplayer.value)return
        viewModelScope.launch(Dispatchers.IO) {
            val bytes: ByteArray = ByteArray(2)
            bytes[0] = message.type.toByte()
            bytes[1] = message.round.toByte()
            val outputStream = btManager.socket!!.outputStream
            try {
                outputStream.write(bytes)
            } catch (e: IOException) {

            }
        }
    }

    fun opponentDisconnected(){
        btManager.updateStatus(3)
        btManager.disconnect()
    }


    override fun onCleared() {
        super.onCleared()
        speechRecognitionManager.onDestroy()
    }


}