package com.example.wordle.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.contentType
import androidx.compose.ui.text.style.LineHeightStyle
import com.example.wordle.ui.theme.WordleGreen

@Composable
fun GameResult(
    gameResult: Boolean,
    secretWord: String?
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ){
        Text(text = (if(gameResult) "Congratulations! You won!" else "You lost!"))
        InputBoard(listOf(secretWord!!.map{ c -> Pair(c, WordleGreen)}))
    }
}
