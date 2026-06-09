package com.example.wordle.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SpeechRecognitionManager(
    private val context: Context
) {
    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    private val _words = MutableStateFlow("")
    val words = _words.asStateFlow()

    private var onResult: ((String)-> Unit)?=null
    private var onError: ((Int)->Unit)?=null

    init{
        recognizer.setRecognitionListener(
            object : RecognitionListener {
                override fun onBeginningOfSpeech() {

                }

                override fun onBufferReceived(p0: ByteArray?) {

                }

                override fun onEndOfSpeech() {

                }

                override fun onError(p0: Int) {
                    onError?.invoke(p0)
                }

                override fun onEvent(p0: Int, p1: Bundle?) {


                }

                override fun onPartialResults(p0: Bundle?) {

                }

                override fun onReadyForSpeech(p0: Bundle?) {

                }

                override fun onResults(p0: Bundle?) {
                    _words.value =
                        (p0?.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                        )?.get(0)?: "").split(' ')[0]
                    if (_words.value.isNotBlank()) {
                        onResult?.invoke(_words.value)
                    }
                }

                override fun onRmsChanged(p0: Float) {

                }

            }
        )

    }

    fun startListening(
        onRes: (String)-> Unit,
        onError: (Int) -> Unit
    ) {
        this.onResult = onRes
        this.onError = onError
        val intent = Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        ).apply{
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "en-US"
            )
        }

        recognizer.startListening(intent)
    }

    fun stopListening(){
        recognizer.stopListening()
    }

    fun onDestroy(){
        recognizer.destroy()
    }

}