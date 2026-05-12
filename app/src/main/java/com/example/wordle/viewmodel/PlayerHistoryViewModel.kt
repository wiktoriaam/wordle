package com.example.wordle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordle.data.local.AppDatabase
import com.example.wordle.data.local.entities.GameRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlayerHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val dao = db.gameHistoryDao()
    var gamesHistory: Flow<List<GameRecord>> = dao.getAllGames()

    fun deleteGamesHistory(){
        viewModelScope.launch(Dispatchers.IO){
            db.clearAllTables()
        }
    }
}