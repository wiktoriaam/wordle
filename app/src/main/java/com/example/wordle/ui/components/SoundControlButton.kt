package com.example.wordle.ui.components

import androidx.compose.foundation.Image
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.example.wordle.R
import com.example.wordle.utils.AudioManager

@Composable
fun SoundControlButton(
    initiallyMuted: Boolean
){
    var audioMuted by remember { mutableStateOf(initiallyMuted) }

    IconButton(
        onClick = {
            audioMuted = !audioMuted
            AudioManager.onSettingsChange()
            AudioManager.playClickSound()
        }
    ) {
        Image(
            painter = painterResource(id = if (audioMuted) R.drawable.muted else R.drawable.unmuted),
            contentDescription = null
        )
    }
}