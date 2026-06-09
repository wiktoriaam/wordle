package com.example.wordle.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameRecord(
    @PrimaryKey(autoGenerate = true) val gameId: Long = 0,
    val gameResult: Boolean,
    val guessed: Boolean,
    val secretWord: String,
    val tries: Int,
    val multiplayer: Boolean,
    val opponentTries: Int,
    val opponentGuessed: Boolean
)
