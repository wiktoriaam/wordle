package com.example.wordle.ui


import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import android.media.AudioManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.example.wordle.viewmodel.BluetoothViewModel
import kotlinx.coroutines.flow.MutableStateFlow


@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BluetoothScreen(
    navController: NavController,
    bluetoothViewModel: BluetoothViewModel
){
    val context = LocalContext.current

    val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    val devicesFound by bluetoothViewModel.devicesFound.collectAsState()
    val gameStarted by bluetoothViewModel.gameStarted.collectAsState()
    var username by remember{ mutableStateOf("WordleUser") }

    LaunchedEffect(gameStarted) {
        if(gameStarted){
            navController.navigate("game")
        }
    }


    val enableBluetooth = @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT) {
        if (!bluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            (context as? Activity)?.startActivityForResult(enableBtIntent, 1)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { granted: Map<String, Boolean> ->
        val grantedPermissions = granted.values.all{ it }
        if(grantedPermissions){
            enableBluetooth()
        }
    }

    val searchingPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { granted: Map<String, Boolean> ->
        val grantedPermissions = granted.values.all{ it }
        if(grantedPermissions){
            bluetoothViewModel.findGame()
        }
    }

    val discoverablePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        res ->
        if(res.resultCode != Activity.RESULT_CANCELED){
            bluetoothViewModel.hostGame()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            com.example.wordle.utils.AudioManager.playClickSound()
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE
                )
            )
        }) {
            Text(text = "1. Turn on bluetooth")
        }
        Button(onClick = {
            com.example.wordle.utils.AudioManager.playClickSound()
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 50)
            }
            discoverablePermissionLauncher.launch(discoverableIntent)
        }) {
            Text(text = "Host a game")
        }
        Text("OR")
        Button(onClick = {
            com.example.wordle.utils.AudioManager.playClickSound()
            searchingPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }) {
            Text(text = "Find a game")
        }

        TextField(
            value = username,
            onValueChange = { username = it
                            bluetoothViewModel.updateName(username)},
            label = { Text("Enter username!") },
            singleLine = true
        )

        for(device in devicesFound){
            if(device!!.name != null){
                Button(
                    onClick = {
                        com.example.wordle.utils.AudioManager.playClickSound()
                        bluetoothViewModel.connectToGame(device) }
                ){
                    Text(device.name)
                }
                }
        }
    }

}


