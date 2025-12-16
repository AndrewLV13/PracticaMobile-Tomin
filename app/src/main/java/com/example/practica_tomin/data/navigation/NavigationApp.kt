package com.example.practica_tomin.data.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavigationApp(navController: NavHostController,
                  signUpViewModel: SignUpViewModel,
                  signInViewModel: SignInViewModel,
                  locationViewModel: LocationViewModel) {
    NavHost(
        navController = navController,
        startDestination = "sign_up"

    ) {
        composable("sign_up") {
            SignUpScreen(
                viewModel = signUpViewModel,
                onSignInClick = { navController.navigate("sign_in") },
                onRegistrationSuccess = {
                    navController.navigate("home") {
                        popUpTo("sign_up") { inclusive = true }
                    }
                }
            )
        }

        composable("sign_in") {
            SignInScreen(
                viewModel = signInViewModel,
                onSignInClick = { navController.navigate("home") },
                onSignUpClick = { navController.navigate("sign_up") }
            )
        }

        composable("home") {
            HomeScreen()
        }

        composable("location") {
            GeodataScreen(
                viewModel = locationViewModel
            )
        }
    }
}