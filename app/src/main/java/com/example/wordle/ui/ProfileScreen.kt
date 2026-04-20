package com.example.wordle.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wordle.ui.theme.WordleTheme

@Composable
fun ProfileScreen(){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "Player profile")
    }
}

@Preview
@Composable
fun ProfileScreenComposable(){
    WordleTheme {
        ProfileScreen()
    }
}