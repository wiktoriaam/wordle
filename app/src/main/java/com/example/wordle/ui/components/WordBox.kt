package com.example.wordle.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wordle.ui.theme.WordleGreen
import com.example.wordle.ui.theme.WordleTheme
import com.example.wordle.ui.theme.WordleYellow

@Composable
fun WordBox(
    wordColors: List<Pair<Char, Color>>
) {
    Row(
        modifier = Modifier.padding(4.dp).graphicsLayer(scaleX = 0.8f, scaleY = 0.8f, transformOrigin = TransformOrigin(0.5f,0.5f)),
        horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally)
    ){
        for((letter, color) in wordColors){
            InputBox(
                bgColor = color,
                textColor = Color.White,
                letter = letter
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordBoxPreview(){
    WordleTheme{
        WordBox(listOf(Pair('A', WordleGreen), Pair('N', WordleYellow)))
    }
}