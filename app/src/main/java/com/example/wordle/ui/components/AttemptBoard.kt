package com.example.wordle.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.wordle.data.local.entities.GameDetails
import com.example.wordle.ui.theme.WordleDarkGray
import com.example.wordle.ui.theme.WordleGreen
import com.example.wordle.ui.theme.WordleLightGray
import com.example.wordle.ui.theme.WordleYellow

@Composable
fun AttemptBoard(
    gameDetails: List<GameDetails>
){
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        items(gameDetails) { game ->
            AttemptBox(game.order, game.guessedWord.zip(game.status) { char1, char2 ->
                Pair(
                    char1, when (char2) {
                        '0' -> WordleGreen
                        '1' -> WordleDarkGray
                        '2' -> WordleLightGray
                        '3' -> WordleYellow
                        else -> WordleLightGray
                    }
                )
            })
        }

    }
}