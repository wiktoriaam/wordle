package com.example.wordle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle.model.LetterState
import com.example.wordle.ui.theme.WordleTheme
import com.example.wordle.ui.theme.WordleYellow

@Composable
fun InputBox(
    bgColor: Color,
    textColor: Color,
    letter: Char,
){

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.width(70.dp).height(70.dp).clip(
            RoundedCornerShape(8.dp)).background(color = bgColor),

    ){
        Text(text = letter.toString(), color = textColor, fontSize = 50.sp)
    }
}


@Preview
@Composable
fun InputBoxPreview(){
    WordleTheme {
        InputBox(
            bgColor = WordleYellow,
            textColor = Color.White,
            letter = 'S'
        )
    }
}