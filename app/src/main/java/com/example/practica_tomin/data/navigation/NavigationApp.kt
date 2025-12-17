package com.example.up_piatnitskii.data.navigation


import SignUpViewModel
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
import com.example.practica_tomin.data.screens.OnboardScreen

import com.example.practice_mobile.ui.screen.ForgotPassword
import com.example.practice_mobile.ui.screen.RegisterAccount
import com.example.practice_mobile.ui.screen.Verification


import com.example.up_piatnitskii.data.screens.SignInScreen
import com.example.up_piatnitskii.data.viewModel.SignInViewModel


@Composable
fun NavigationApp(navController: NavHostController,
                  signUpViewModel: SignUpViewModel,
                  signInViewModel: SignInViewModel,
                  context: Context) {
    NavHost(
        navController = navController,
        startDestination = "start_menu"

    ) {
        composable("start_menu") {
            OnboardScreen (
                onGetStartedClick = { navController.navigate("sign_up") },
            )
        }
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
                },
                onBackClick = {
                    navController.navigate("start_menu")
                }
            )
        }

        composable("sign_in") {
            SignInScreen(
                viewModel = signInViewModel,
                onBackClick = { navController.popBackStack() },
                onRegisterClick = { navController.navigate("sign_up") },
                onSignInClick = { navController.navigate("home") },
                onForgotPasswordClick = { navController.navigate("ForgotPassword") }
            )
        }
        composable("Verivication") {
            Verification()
        }


        composable("ForgotPassword") {
            ForgotPassword(
                onOTPClick = {navController.navigate("Verivication")}
            )
        }

    }
}