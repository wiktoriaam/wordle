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
import com.example.wordle.ui.ProfileScreen
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
                    navController.navigate("mode")
                },
                onProfileClick = {
                    navController.navigate("profile")
                }
            )
        }
        composable(route = "mode"){
            GameModeScreen(
                onSingleplayerClick = {
                    navController.navigate("game")
                },
                onMultiplayerClick = {
                    navController.navigate("game")
                }
            )
        }
        composable(route = "game"){
            val viewModel: GameViewModel = viewModel()
            GameScreen(viewModel, navController)
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
                    navController.navigate("menu")
                },
                onPlayAgain = {
                    navController.navigate("game")
                })
        }
        composable(route = "profile"){
            ProfileScreen()
        }
    }
}