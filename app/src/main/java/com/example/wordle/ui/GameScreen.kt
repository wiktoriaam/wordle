package com.example.wordle.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wordle.R
import com.example.wordle.ui.components.InputBoard
import com.example.wordle.ui.components.Keyboard
import com.example.wordle.utils.AudioManager
import com.example.wordle.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel(),
               navController: NavController){
    val letterColors by viewModel.letterColors.collectAsState()
    val inputBoxColors by viewModel.inputBoxColors.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val gameFinished by viewModel.gameFinished.collectAsState()
    val secretWord by viewModel.secretWord.collectAsState()
    val gameResult by viewModel.gameResult.collectAsState()
    val multiplayer by viewModel.multiplayer.collectAsState()
    val opponentState by viewModel.opponentStatus.collectAsState()
    val opponentRound by viewModel.opponentRound.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        permissionGranted -> if(permissionGranted){
            viewModel.onMicrophoneClick {}
    }
    }

    LaunchedEffect(Unit) {
        if(multiplayer)viewModel.listenToOpponent()
    }
    LaunchedEffect(gameFinished) {
        if(gameFinished ){
            navController.navigate("end/$secretWord/$gameResult/$multiplayer/$opponentState/$opponentRound")
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if(multiplayer){
            when (opponentState) {
                0 -> Text("Opponent's result: $opponentRound. try")
                1 -> Text("Opponent guessed correctly in $opponentRound tries!")
                2 -> Text("Opponent didn't guess their word!")
                3 -> Text("Opponent disconnected")
            }
        }
        if(!multiplayer && opponentState==3){
            Text("Opponent disconnected")
        }
        InputBoard(
            inputBoxColors = inputBoxColors
        )

        Text(text = errorMessage, fontSize = 15.sp)

        Keyboard(
            letterColors = letterColors,
            onKeyClick = viewModel::onKeyClick
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
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
            IconButton(
                onClick = {viewModel.onMicrophoneClick{
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.microphone),
                    contentDescription = null
                )
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

        Button(
            onClick = {
                AudioManager.playClickSound()
                viewModel.btManager.disconnect()
                viewModel.btManager.onNewGame()
                navController.navigate("menu")
            }
        ){
            Text("QUIT GAME")
        }
    }
}

