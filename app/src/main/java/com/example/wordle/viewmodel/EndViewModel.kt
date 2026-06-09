package com.example.wordle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.wordle.utils.BTManager

class EndViewModel(
    application: Application,
    val btManager: BTManager
) : AndroidViewModel(application) {

}