package com.example.wordle.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.wordle.ui.components.GameResult

@Composable
fun EndScreen(
    gameResult: Boolean,
    secretWord: String?,
    onBackToMenu: () -> Unit,
    onPlayAgain: () -> Unit
){
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