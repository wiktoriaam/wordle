package com.example.wordle.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wordle.ui.components.GameRecordBoard
import com.example.wordle.ui.components.GameRecordRow
import com.example.wordle.utils.AudioManager
import com.example.wordle.viewmodel.PlayerHistoryViewModel

@Composable
fun GameHistoryScreen(
    navController: NavController,
    viewModel: PlayerHistoryViewModel = viewModel()
){
    val games by viewModel.gamesHistory.collectAsStateWithLifecycle(initialValue = emptyList())
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        GameRecordBoard(
            games,
            navController
        )


        Button(
            onClick = {
                AudioManager.playClickSound()
                viewModel.deleteGamesHistory()
            }
        ){
            Text(text = "Delete history")
        }
        Button(
            onClick = {
                AudioManager.playClickSound()
                navController.navigate("menu")
            }
        ){
            Text(text = "Back to MENU page")
        }
    }
}

