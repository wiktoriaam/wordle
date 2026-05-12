package com.example.wordle.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wordle.ui.theme.WordleGreen

@Composable
fun GameRecordRow(
    guessNr: Int,
    gameResult: Boolean,
    secretWord: String,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier.clickable(
            onClick = onClick
        ).fillMaxWidth()
        .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Text(text = "$guessNr", textAlign = TextAlign.Center)
        WordBox(secretWord.map{letter -> letter to WordleGreen })
        Text(text = if(gameResult) "WON" else "LOST", textAlign = TextAlign.Center)
    }
}