package com.example.wordle.utils

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.ui.graphics.Color
import com.example.wordle.data.local.AppDatabase
import com.example.wordle.data.local.entities.GameDetails
import com.example.wordle.data.local.entities.GameRecord
import com.example.wordle.model.Settings
import com.example.wordle.ui.theme.WordleDarkGray
import com.example.wordle.ui.theme.WordleGreen
import com.example.wordle.ui.theme.WordleLightGray
import com.example.wordle.ui.theme.WordleYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream
import kotlin.collections.get
import kotlin.collections.set
import kotlin.text.set


class BTManager(
    private val context: Context
) {
    val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private val _devicesFound = MutableStateFlow<Set<BluetoothDevice?>>(emptySet())
    val devicesFound: StateFlow<Set<BluetoothDevice?>> = _devicesFound
    var serverSocket: BluetoothServerSocket? = null
    var socket: BluetoothSocket? = null
    private val _gameStarted = MutableStateFlow<Boolean>(false)
    val gameStarted : StateFlow<Boolean> = _gameStarted
    private val _opponentRound = MutableStateFlow(1)
    private val _opponentStatus = MutableStateFlow(0)
    val opponentRound: StateFlow<Int> = _opponentRound
    val opponentStatus: StateFlow<Int> = _opponentStatus
    private val _gameFinished = MutableStateFlow(false)
    val gameFinished: StateFlow<Boolean> = _gameFinished
    private val _myGameFinished = MutableStateFlow(false)
    val myGameFinished: StateFlow<Boolean> = _myGameFinished
    private val _gameResult = MutableStateFlow(false)
    val gameResult: StateFlow<Boolean> = _gameResult

    private val _secretWord = MutableStateFlow("")
    val secretWord: StateFlow<String> = _secretWord
    private val _finishedBeforeOpponent = MutableStateFlow(false)
    val finishedBeforeOpponent: StateFlow<Boolean> = _finishedBeforeOpponent
    @Volatile
    var myWordGuessed = false
    @Volatile
    var currentGuess = 0

    private val _inputBoxColors = MutableStateFlow(List(Settings.GUESSES_NR){
        List(Settings.WORD_LENGTH){Pair(' ', WordleLightGray)}
    })
    val inputBoxColors: StateFlow<List<List<Pair<Char, Color>>>> = _inputBoxColors




    private val receiver = object : BroadcastReceiver(){
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onReceive(p0: Context?, p1: Intent?) {
            val action: String? = p1?.action
            when(action){
                BluetoothDevice.ACTION_FOUND -> {
                    val device = p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)

                    _devicesFound.value = _devicesFound.value.toMutableSet() + device
                }
            }
        }
    }




    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun startHosting(
        username: String,
        scope: CoroutineScope
    ){
        scope.launch(Dispatchers.IO){
            try{
                bluetoothAdapter?.setName(username)
                serverSocket = bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord("WORDLE", Settings.uuid)
                val sckt = serverSocket?.accept(60000)//one minute
                socket = sckt
                serverSocket?.close()
                startMultiplayerGame()

            }
            catch(e: IOException){
                e.toString().let { Log.d("BLUE ERROR", it) }
                serverSocket?.close()
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])
    fun startSearching(
        username: String
    ){
        if(bluetoothAdapter?.isEnabled == true) {
            bluetoothAdapter.setName(username)
            if(bluetoothAdapter.isDiscovering()){
                bluetoothAdapter.cancelDiscovery()
            }

            val filter = IntentFilter(
                BluetoothDevice.ACTION_FOUND
            )
            context.registerReceiver(receiver, filter)
            bluetoothAdapter.startDiscovery()
        }

    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun connectToServer(
        device: BluetoothDevice,
        scope: CoroutineScope
    ){
        scope.launch(Dispatchers.IO) {
            val sckt = device.createRfcommSocketToServiceRecord(Settings.uuid)
            bluetoothAdapter?.cancelDiscovery()
            sckt.connect()
            socket = sckt
            startMultiplayerGame()
        }
    }

    fun disconnect(){
        if(_gameStarted.value){
            val outputStream : OutputStream? = socket?.outputStream

            try {
                outputStream?.close()
            } catch (e: IOException ){}

            try {
                socket?.close()
            } catch (e: IOException){}

            socket = null
            _gameStarted.value = false
        }
    }

    fun destroy(){
        try{
            context.unregisterReceiver(receiver)
        }
        catch(e: Exception){

        }
        socket?.close()
        serverSocket?.close()
    }

    fun startMultiplayerGame(){
        _gameStarted.tryEmit(true)
        Log.d("GAME", "game started")
    }

    fun updateStatus(
        new: Int
    ){
        _opponentStatus.value = new
    }

    fun updateRound(
        new: Int
    ){
        _opponentRound.value = new
    }

    fun onGameFinished(){
        _gameFinished.value = true
    }

    fun onMyGameFinished(){
        _myGameFinished.value = true
    }
    fun onNewGame(){
        _gameFinished.value = false
        _myGameFinished.value = false
        _opponentStatus.value = 0
        _opponentRound.value = 1
        _secretWord.value = ""
        _gameResult.value = false
        currentGuess = 0
        myWordGuessed = false
        _inputBoxColors.value = List(Settings.GUESSES_NR){
            List(Settings.WORD_LENGTH){Pair(' ', WordleLightGray)}
        }
        _finishedBeforeOpponent.value = false
    }

    fun revealSecretWord(
        new: String
    ){
        _secretWord.value = new
    }

    fun setGameResult(
        new: Boolean
    ){
        _gameResult.value = new
    }

    fun updateGuess(){
        currentGuess+=1
    }
    fun guessed(){
        myWordGuessed=true
    }

    fun updateInput(
        new: String,
        color: Color,
        idx: Int
    ){
            val newRow = _inputBoxColors.value[currentGuess].toMutableList().apply { this[idx] = Pair(new[idx], color) }
            val newInputBoxColors = _inputBoxColors.value.toMutableList().apply { this[currentGuess] = newRow }
            _inputBoxColors.value = newInputBoxColors
    }
    fun updateLetter(
        new: Char,
        color: Color,
        idx: Int
    ){
        val newRow = _inputBoxColors.value[currentGuess].toMutableList().apply { this[idx] = Pair(new, color) }
        val newInputBoxColors = _inputBoxColors.value.toMutableList().apply { this[currentGuess] = newRow }
        _inputBoxColors.value = newInputBoxColors
    }

    fun finishedBefore(){
        _finishedBeforeOpponent.value = true
    }



}