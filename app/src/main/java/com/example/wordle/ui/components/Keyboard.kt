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
import com.example.wordle.model.LetterState
import com.example.wordle.model.Settings
import com.example.wordle.ui.theme.WordleDarkGray
import com.example.wordle.ui.theme.WordleGreen
import com.example.wordle.ui.theme.WordleLightGray
import com.example.wordle.ui.theme.WordleTheme

@Composable
fun Keyboard(
    letterColors: Map<Char, Color>,
    onKeyClick: (Char) -> Unit
){
     Column(
         horizontalAlignment = Alignment.CenterHorizontally,
         modifier = Modifier.fillMaxWidth().padding(vertical=20.dp)
     ){
         for (row in Settings.KEYBOARD_LAYOUT){
             Row(
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(vertical = 4.dp, horizontal = 4.dp),
                 horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally)
             ){
                 for(c in row){
                     Key(
                         bgColor = letterColors[c] ?: WordleLightGray,
                         textColor = Color.White,
                         letter = c,
                         onClick = onKeyClick,
                         modifier = Modifier.weight(1f)
                     )
                 }
             }
         }
     }
}

@Preview
@Composable
fun KeyboardPreview(){
    WordleTheme {
        Keyboard(
            letterColors = mapOf('A' to WordleGreen),
            onKeyClick = {}
        )}
}