package com.example.up_piatnitskii.data.navigation


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.practica_tomin.data.viewModel.SignInViewModel
import com.example.practica_tomin.data.viewModel.SignUpViewModel
import com.example.practice_mobile.ui.screen.ForgotPassword
import com.example.practice_mobile.ui.screen.RegisterAccount

import com.example.up_piatnitskii.data.screens.SignInScreen



@Composable
fun NavigationApp(navController: NavHostController,
                  signUpViewModel: SignUpViewModel,
                  signInViewModel: SignInViewModel,
                  context: Context) {
    NavHost(
        navController = navController,
        startDestination = "sign_up"

    ) {
        composable("sign_up") {
            RegisterAccount(
                viewModel = signUpViewModel,

                onRegistrationSuccess = {
                    navController.navigate("sign_in") {
                        popUpTo("sign_up") { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate("sign_in")
                }
            )
        }
        composable("sign_in") {
            SignInScreen(
                viewModel = signInViewModel,
                onBackClick = { navController.popBackStack() },
                onRegisterClick = { navController.navigate("sign_up") },
                onSignInClick = { navController.navigate("home") },
                onForgotPasswordClick = { navController.navigate("forgot_password") }
            )
        }


        composable("ForgotPassword") {
            ForgotPassword()
        }
        composable("sign_in") {
            SignInScreen(
                viewModel = signInViewModel,
                onSignInClick = {navController.navigate("sign_up")}
            )
        }
    }
}