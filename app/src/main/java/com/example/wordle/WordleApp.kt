package com.example.wordle

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wordle.model.GameStatus
import com.example.wordle.ui.EndScreen
import com.example.wordle.ui.GameModeScreen
import com.example.wordle.ui.GameScreen
import com.example.wordle.ui.MainScreen
import com.example.wordle.ui.ProfileScreen
import com.example.wordle.viewmodel.GameViewModel

enum class WordleScreen(){
    Menu,
    Mode,
    Game,
    End,
    Profile
}
@Composable
fun WordleApp(
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination = WordleScreen.Menu.name
    ){
        composable(route = WordleScreen.Menu.name){
            MainScreen(
                onGameModeClick = {
                    navController.navigate(WordleScreen.Mode.name)
                },
                onProfileClick = {
                    navController.navigate(WordleScreen.Profile.name)
                }
            )
        }
        composable(route = WordleScreen.Mode.name){
            GameModeScreen(
                onSingleplayerClick = {
                    navController.navigate(WordleScreen.Game.name)
                },
                onMultiplayerClick = {
                    navController.navigate(WordleScreen.Game.name)
                }
            )
        }
        composable(route = WordleScreen.Game.name){
            val viewModel: GameViewModel = viewModel()
            GameScreen(viewModel)
        }
        composable(route = WordleScreen.End.name){
            EndScreen(
                gameStatus = GameStatus.WIN
            )
        }
        composable(route = WordleScreen.Profile.name){
            ProfileScreen()
        }
    }
}