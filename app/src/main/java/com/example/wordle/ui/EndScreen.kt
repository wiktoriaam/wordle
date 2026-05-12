package com.example.wordle.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.wordle.ui.components.GameResult
import com.example.wordle.utils.AudioManager

@Composable
fun EndScreen(
    gameResult: Boolean,
    secretWord: String?,
    onBackToMenu: () -> Unit,
    onPlayAgain: () -> Unit
){
    LaunchedEffect(Unit) {
        AudioManager.playEndSound(gameResult)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GameResult(gameResult, secretWord)

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onBackToMenu
            ) {
                Text(text = "Go back to MENU page")
            }
            Button(
                onClick = onPlayAgain
            ) {
                Text(text = "Play again")
            }
        }
    }
}