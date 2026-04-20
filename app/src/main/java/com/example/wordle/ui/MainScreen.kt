package com.example.wordle.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wordle.ui.theme.WordleTheme

@Composable
fun MainScreen(
    onGameModeClick: () -> Unit,
    onProfileClick: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "WELCOME TO WORDLE!")
        Button(
            onClick = onGameModeClick
        ) {
            Text(text = "Choose game mode")
        }
        Button(
            onClick = onProfileClick
        ) {
            Text(text = "See previous results")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview(){
    WordleTheme {
        MainScreen(
            onGameModeClick = {},
            onProfileClick = {}
        )
    }
}