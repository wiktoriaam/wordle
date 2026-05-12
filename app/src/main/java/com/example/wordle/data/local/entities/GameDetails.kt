package com.example.wordle.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "details",
    foreignKeys = [
        ForeignKey(
            entity = GameRecord::class,
            parentColumns = ["gameId"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GameDetails (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val gameId: Long,
    val guessedWord: String,
    val status: String,
    val order: Int
)