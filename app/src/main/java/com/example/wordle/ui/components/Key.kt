package com.example.wordle.ui.components



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle.ui.theme.WordleDarkGray
import com.example.wordle.ui.theme.WordleTheme

@Composable
fun Key(
    bgColor: Color,
    textColor: Color,
    letter: Char,
    onClick: (Char) -> Unit,
    modifier: Modifier = Modifier
){

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.clickable(
            onClick = {onClick(letter)}
        ).width(35.dp).height(50.dp).clip(RoundedCornerShape(8.dp)).background(color = bgColor)
    ){
        Text(text = letter.toString(), color = textColor, fontSize = 30.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun KeyPreview(){
    WordleTheme {
        Key(
            bgColor = WordleDarkGray,
            textColor = Color.White,
            onClick = {},
            letter = 'A',
            modifier = Modifier
        )
    }
}