package com.example.practica_tomin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.practica_tomin.ui.theme.PracticaTominTheme
import com.example.compose_supabase.data.model.StudentApplication
import com.example.practica_tomin.data.viewModel.SignInViewModel
import com.example.practica_tomin.data.viewModel.SignUpViewModel
import com.example.up_piatnitskii.data.navigation.NavigationApp

class MainActivity : ComponentActivity() {
    val signUpViewModel by viewModels<SignUpViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val signUpViewModel = SignUpViewModel(StudentApplication.database.userDao())
        val signInViewModel = SignInViewModel(StudentApplication.database.userDao())

        enableEdgeToEdge()

        setContent {
            PracticaTominTheme {

                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    NavigationApp(navController = navController, signUpViewModel = signUpViewModel, signInViewModel = signInViewModel, context = context)
                }
            }
        }
    }
}
