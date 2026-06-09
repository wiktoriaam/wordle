package com.example.wordle

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wordle.ui.BluetoothScreen
import com.example.wordle.ui.EndScreen
import com.example.wordle.ui.GameModeScreen
import com.example.wordle.ui.GameScreen
import com.example.wordle.ui.MainScreen
import com.example.wordle.ui.GameHistoryScreen
import com.example.wordle.ui.GameDetailsScreen
import com.example.wordle.ui.SettingsScreen
import com.example.wordle.utils.AudioManager
import com.example.wordle.utils.BTManager
import com.example.wordle.viewmodel.BluetoothViewModel
import com.example.wordle.viewmodel.EndViewModel
import com.example.wordle.viewmodel.GameViewModel



@SuppressLint("ViewModelConstructorInComposable")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun WordleApp(
    navController: NavHostController = rememberNavController(),
    btManager: BTManager
){
    val context = LocalContext.current
    val application = context.applicationContext as Application
    NavHost(
        navController = navController,
        startDestination = "menu"
    ){
        composable(route = "menu"){
            MainScreen(
                onGameModeClick = {
                    AudioManager.playClickSound()
                    navController.navigate("mode")
                },
                onProfileClick = {
                    AudioManager.playClickSound()
                    navController.navigate("gameHistory")
                },
                onSettingsClick = {
                    AudioManager.playClickSound()
                    navController.navigate("settings")
                }
            )
        }
        composable(route = "mode"){
            GameModeScreen(
                onSingleplayerClick = {
                    AudioManager.playClickSound()
                    navController.navigate("game")
                },
                onMultiplayerClick = {
                    AudioManager.playClickSound()
                    navController.navigate("bluetooth")
                }
            )
        }
        composable(route = "game"){
            GameScreen(navController = navController, viewModel = GameViewModel(btManager=btManager, application=application))
        }
        composable(route = "end/{secretWord}/{result}/{multiplayer}/{opponentState}/{opponentRound}",
            arguments = listOf(navArgument("secretWord") {type = NavType.StringType},
            navArgument("result") {type = NavType.BoolType},
                navArgument("multiplayer") {type = NavType.BoolType},
                navArgument("opponentState") {type = NavType.IntType},
                navArgument("opponentRound") {type = NavType.IntType})){
            backStackEntry -> val secretWord = backStackEntry.arguments?.getString("secretWord")
            val result = backStackEntry.arguments?.getBoolean("result")
            val multiplayer = backStackEntry.arguments?.getBoolean("multiplayer")
            val opponentState = backStackEntry.arguments?.getInt("opponentState")
            val opponentRound = backStackEntry.arguments?.getInt("opponentRound")

            EndScreen(
                gameResult = result == true,
                secretWord = secretWord,
                multiplayer = multiplayer == true,
                opponentState = opponentState,
                opponentRound = opponentRound,
                navController = navController,
                viewModel = EndViewModel(btManager = btManager, application = application)
             )
        }
        composable(route = "gameHistory"){
            GameHistoryScreen(navController = navController)
        }

        composable(route = "gameDetails/{gameId}",
            arguments = listOf(
                navArgument("gameId") { type = NavType.LongType }
            )){
            GameDetailsScreen(navController = navController)
        }

        composable(route = "settings"){
            SettingsScreen(navController)
        }

        composable(route = "bluetooth"){
            BluetoothScreen(navController = navController, bluetoothViewModel = BluetoothViewModel(btManager = btManager, application = application))
        }
    }
}