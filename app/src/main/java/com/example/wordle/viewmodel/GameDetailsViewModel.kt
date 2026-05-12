package com.example.wordle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.wordle.data.local.AppDatabase
import com.example.wordle.data.local.entities.GameDetails
import com.example.wordle.data.local.entities.GameRecord
import kotlinx.coroutines.flow.Flow

class GameDetailsViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    val gameId: Long = savedStateHandle.get<Long>("gameId") ?: 1
    private val db = AppDatabase.getInstance(application)
    private val dao = db.gameHistoryDao()

    var gameDetails: Flow<List<GameDetails>> = dao.getGameDetails(gameId)
    var gameRecord: Flow<List<GameRecord>> = dao.getGameRecord(gameId)
}