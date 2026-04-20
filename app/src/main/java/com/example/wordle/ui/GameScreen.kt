package com.example.wordle.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wordle.ui.components.InputBoard
import com.example.wordle.ui.components.Keyboard
import com.example.wordle.ui.theme.WordleTheme
import com.example.wordle.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel,
               navController: NavController){
    val letterColors by viewModel.letterColors.collectAsState()
    val inputBoxColors by viewModel.inputBoxColors.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val gameFinished by viewModel.gameFinished.collectAsState()
    val secretWord by viewModel.secretWord.collectAsState()
    val gameResult by viewModel.gameResult.collectAsState()

    LaunchedEffect(gameFinished) {
        if(secretWord != null){
            navController.navigate("end/$secretWord/$gameResult")
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        InputBoard(
            inputBoxColors = inputBoxColors
        )

        Text(text = errorMessage, fontSize = 30.sp)

        Keyboard(
            letterColors = letterColors,
            onKeyClick = viewModel::onKeyClick
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { viewModel.onBackSpaceClick() }
            ) {
                Text(text = "ERASE")
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = { viewModel.onEnterClick() }
            ) {
                Text(text = "ENTER")
            }
        }
    }
}

