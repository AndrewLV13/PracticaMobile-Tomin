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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.practica_tomin.data.screens.CategoryProductsScreen

import com.example.practica_tomin.data.screens.OnboardScreen
import com.example.practica_tomin.data.screens.ProductDetailScreen
import com.example.practica_tomin.data.viewModel.SignUpViewModel
import com.example.practice_mobile.ui.screen.ForgotPassword
import com.example.practice_mobile.ui.screen.RegisterAccount
import com.example.practice_mobile.ui.screen.Verification
import com.example.shoeshop.ui.screens.HomeScreen


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
                onOTPClick = {navController.navigate("Verivication")},
                onBackClick = { navController.navigate("sign_in")  }
            )

        }
        composable("home") {
            HomeScreen(
                onProductClick = { product ->
                    // Навигация на экран товара
                    navController.navigate("product/${product.id}")
                },
                onCartClick = {
                    // Навигация на корзину
                    // navController.navigate("cart")
                },
                onSearchClick = {
                    // Навигация на поиск
                    // navController.navigate("search")
                },
                onCategoryClick = { categoryName ->
                    // Навигация на экран категории
                    navController.navigate("category/$categoryName")
                }
            )
        }
        composable(
            route = "category/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryProductsScreen(
                categoryName = categoryName,
                onProductClick = { product ->
                    // Навигация на экран товара
                    navController.navigate("product/${product.id}")
                },
                onBackClick = { navController.popBackStack() },
                onCategorySelected = { newCategoryName ->
                    // Навигация на другую категорию
                    navController.navigate("category/$newCategoryName") {
                        // Очищаем стек чтобы не было много экранов категорий
                        popUpTo("category/{categoryName}") { inclusive = true }
                    }
                }
            )
        }

        // Добавьте новый маршрут для деталей товара
        composable(
            route = "product/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                onBackClick = { navController.popBackStack() },
                onAddToCart = {
                },
                onToggleFavorite = {
                }
            )
        }


    }
}