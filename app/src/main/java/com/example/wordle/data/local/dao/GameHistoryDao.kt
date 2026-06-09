package com.example.wordle.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.wordle.data.local.entities.GameDetails
import com.example.wordle.data.local.entities.GameRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHistoryDao {
    @Query("SELECT * FROM games")
    fun getAllGames(): Flow<List<GameRecord>>

    @Query("SELECT * FROM details WHERE gameId=:gameId ORDER BY gameId")
    fun getGameDetails(gameId: Long): Flow<List<GameDetails>>

    @Query("SELECT * FROM games WHERE gameId=:gameId")
    fun getGameRecord(gameId: Long): Flow<List<GameRecord>>

    @Query("SELECT * FROM games ORDER BY tries ASC LIMIT 1")
    fun getBestResult(): Flow<List<GameRecord>>

    @Query("DELETE FROM details")
    suspend fun deleteGameDetails()

    @Query("DELETE FROM games")
    suspend fun deleteGameRecord()


    @Insert
    suspend fun insertGame(game: GameRecord): Long

    @Insert
    suspend fun insertGameDetails(gameDetails: GameDetails)
}