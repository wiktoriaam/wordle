package com.example.wordle.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.wordle.ui.components.AttemptBoard
import com.example.wordle.ui.components.AttemptBox
import com.example.wordle.ui.components.WordBox
import com.example.wordle.ui.theme.WordleDarkGray
import com.example.wordle.ui.theme.WordleGreen
import com.example.wordle.ui.theme.WordleLightGray
import com.example.wordle.ui.theme.WordleYellow
import com.example.wordle.utils.AudioManager
import com.example.wordle.viewmodel.GameDetailsViewModel
import kotlin.collections.emptyList

@Composable
fun GameDetailsScreen(
    viewModel: GameDetailsViewModel = viewModel(),
    navController: NavController
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val gameDetails by viewModel.gameDetails.collectAsStateWithLifecycle(initialValue = emptyList())

        AttemptBoard(gameDetails)

        val gameRecord by viewModel.gameRecord.collectAsStateWithLifecycle(initialValue = emptyList())
        for (game in gameRecord) {
            if (!game.gameResult) {
                Text("LOST")
                WordBox(game.secretWord.map { letter -> letter to WordleGreen })
            }
            else{
                Text("WON")
            }
            if(game.multiplayer){
                val extraInfo = if(game.opponentGuessed) "guessed in ${game.opponentTries} tries!" else "Didn't guess!"
                Text("Opponent result: $extraInfo")
            }
        }
        Button(
            onClick = {
                AudioManager.playClickSound()
                navController.navigate("gameHistory")
            }
        ){
            Text(text = "Back")
        }
    }
}