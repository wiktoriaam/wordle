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
import androidx.navigation.NavController
import com.example.wordle.ui.components.GameResult
import com.example.wordle.utils.AudioManager
import com.example.wordle.viewmodel.EndViewModel

@Composable
fun EndScreen(
    gameResult: Boolean,
    secretWord: String?,
    multiplayer: Boolean,
    opponentState: Int?,
    opponentRound: Int?,
    navController: NavController,
    viewModel: EndViewModel
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
                onClick = {
                    viewModel.btManager.disconnect()
                    viewModel.btManager.onNewGame()
                    navController.navigate("menu")
                }
            ) {
                Text(text = "Go back to MENU page")
            }
            if(!multiplayer) {
                Button(
                    onClick = {
                        viewModel.btManager.onNewGame()
                        navController.navigate("game")
                    }
                ) {
                    Text(text = "Play again")
                }
            }
        }
    }
}