package com.example.wordle

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wordle.ui.EndScreen
import com.example.wordle.ui.GameModeScreen
import com.example.wordle.ui.GameScreen
import com.example.wordle.ui.MainScreen
import com.example.wordle.ui.GameHistoryScreen
import com.example.wordle.ui.GameDetailsScreen
import com.example.wordle.ui.SettingsScreen
import com.example.wordle.utils.AudioManager
import com.example.wordle.viewmodel.GameDetailsViewModel
import com.example.wordle.viewmodel.GameViewModel



@Composable
fun WordleApp(
    navController: NavHostController = rememberNavController()
){
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
                    navController.navigate("game")
                }
            )
        }
        composable(route = "game"){
            GameScreen(navController = navController)
        }
        composable(route = "end/{secretWord}/{result}",
            arguments = listOf(navArgument("secretWord") {type = NavType.StringType},
            navArgument("result") {type = NavType.BoolType})){
            backStackEntry -> val secretWord = backStackEntry.arguments?.getString("secretWord")
            val result = backStackEntry.arguments?.getBoolean("result")

            EndScreen(
                gameResult = result == true,
                secretWord = secretWord,
                onBackToMenu = {
                    AudioManager.stopAll()
                    AudioManager.playClickSound()
                    navController.navigate("menu")
                },
                onPlayAgain = {
                    AudioManager.playClickSound()
                    navController.navigate("game")
                })
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
    }
}