package com.example.practica_tomin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.practica_tomin.ui.theme.PracticaTominTheme
import com.example.practice_mobile.ui.screen.RegisterAccount
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.practice_mobile.ui.screen.SignIn

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaTominTheme {
                AppNavigation()

            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "register"
    ) {
        composable("register") {
            RegisterAccount(
                onBackClick = { /* логика назад */ },
                onLoginClick = {
                    navController.navigate("login")
                },
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }  // Удаляем register
                    }
                },
                onRegisterClick = { name, email, password ->
                    // TODO: Ваша логика регистрации API
                    println("Регистрация: $name, $email, $password")
                }
            )
        }

        composable("login") {
            SignIn(

            )
        }
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
    PracticaTominTheme {
        Greeting("Android")
    }
}