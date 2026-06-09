package com.example.wordle

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wordle.data.local.AppDatabase
import com.example.wordle.ui.theme.WordleTheme
import com.example.wordle.utils.AudioManager
import com.example.wordle.utils.BTManager

class MainActivity : ComponentActivity() {
    var btManager: BTManager? = null
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AudioManager.initialize(applicationContext)
        btManager = BTManager(applicationContext)
        enableEdgeToEdge()
        setContent {
            WordleApp(btManager = btManager!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        btManager!!.destroy()
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WordleTheme {
        Greeting("Android")
    }
}