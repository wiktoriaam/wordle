package com.example.wordle.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.wordle.R

object AudioManager {
    var audioMuted by mutableStateOf(false)

    private var soundPool: SoundPool? = null
    private var soundMap = mutableMapOf<String,Int>()
    private var currentStreamId: Int? = 0

    fun initialize(context: Context){
        if (soundPool != null)return

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()

        soundPool?.let { pool ->
            soundMap["click"] = pool.load(context, R.raw.click, 1)
            soundMap["win"] = pool.load(context, R.raw.victory, 1)
            soundMap["error"] = pool.load(context, R.raw.error, 1)
        }
    }
    fun playClickSound(){
        if (audioMuted) return

        val sound = soundMap["click"] ?: return
        currentStreamId = soundPool?.play(sound, 1f,1f,1,0,1f)
    }
    fun playEndSound(won: Boolean){
        if (audioMuted) return

        val sound =(if (won) soundMap["win"] else soundMap["error"]) ?: return
        currentStreamId = soundPool?.play(sound, 1f,1f,1,0,1f)
    }

    fun stopAll() {
        soundPool?.let { pool ->
            if (currentStreamId != 0) {
                try { pool.stop(currentStreamId!!) } catch (_: Exception) {}
                currentStreamId = 0
            }
        }
    }

    fun onSettingsChange() {
        audioMuted = !audioMuted
    }
}