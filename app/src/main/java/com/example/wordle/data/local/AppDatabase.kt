package com.example.wordle.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wordle.data.local.dao.GameHistoryDao
import com.example.wordle.data.local.entities.GameDetails
import com.example.wordle.data.local.entities.GameRecord


@Database(entities = [GameRecord::class, GameDetails::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameHistoryDao(): GameHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase ?= null

        fun getInstance(context: Context) : AppDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "games.db"
                    ).build()
                }
                INSTANCE = instance
                return instance
            }
        }
    }
}