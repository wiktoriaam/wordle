package com.example.wordle.viewmodel

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordle.utils.BTManager
import kotlinx.coroutines.flow.StateFlow

class BluetoothViewModel (
    application: Application,
    val btManager: BTManager
    ) : AndroidViewModel(application){

    val devicesFound: StateFlow<Set<BluetoothDevice?>> = btManager.devicesFound
    val gameStarted: StateFlow<Boolean> = btManager.gameStarted
    var username: String = "WordleUser"

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun hostGame(){
        btManager.startHosting(username, viewModelScope)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf=[Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])
    fun findGame(){
        btManager.startSearching(username)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun connectToGame(
        device: BluetoothDevice
    ){
        btManager.connectToServer(device, viewModelScope)
    }

    fun updateName(
        newName: String
    ){
        username = newName
    }
}