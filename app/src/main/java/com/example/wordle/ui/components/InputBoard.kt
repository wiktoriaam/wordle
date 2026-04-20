package com.example.wordle.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wordle.ui.theme.WordleGreen
import com.example.wordle.ui.theme.WordleTheme
import com.example.wordle.ui.theme.WordleYellow

@Composable
fun InputBoard(
    inputBoxColors: List<List<Pair<Char, Color>>>
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        for (rowColors in inputBoxColors){
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally)
            ){
                for((letter, color) in rowColors){
                    InputBox(
                        bgColor = color,
                        textColor = Color.White,
                        letter = letter
                    )
            }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun InputBoardPreview(){
    WordleTheme{
        InputBoard(listOf(listOf(Pair('A', WordleGreen), Pair('N', WordleYellow)), listOf(Pair('S', WordleGreen))))
    }
}
