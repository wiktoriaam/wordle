package com.example.wordle.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.wordle.ui.components.SoundControlButton
import com.example.wordle.utils.AudioManager

@Composable
fun SettingsScreen(
    navController: NavController
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        SoundControlButton(
            AudioManager.audioMuted
        )
        Button(
            onClick = {
                AudioManager.playClickSound()
                navController.navigate("menu")
            }
        ){
            Text(text = "Back")
        }
    }
}