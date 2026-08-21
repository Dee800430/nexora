package com.nexora.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexora.app.core.storage.TokenStorage
import com.nexora.app.presentation.cart.CartScreen
import com.nexora.app.presentation.home.HomeScreen
import com.nexora.app.presentation.home.HomeViewModel
import com.nexora.app.presentation.invoices.InvoiceListScreen
import com.nexora.app.presentation.login.LoginScreen

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val CART = "cart"
    const val INVOICES = "invoices"
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val startDestination =
        if (TokenStorage.isLoggedIn()) {
            Routes.HOME
        } else {
            Routes.LOGIN
        }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Routes.LOGIN) {

            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            val viewModel: HomeViewModel = viewModel()
            HomeScreen(
                viewModel = viewModel,
                onCartClick = {
                    navController.navigate(Routes.CART)
                },
                onInvoicesClick = {
                    navController.navigate(Routes.INVOICES)
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.CART) {
            CartScreen(
                onBack = {
                    navController.popBackStack()
                },
                onOrderComplete = {
                    navController.navigate(Routes.INVOICES) {
                        popUpTo(Routes.HOME)
                    }
                }
            )
        }

        composable(Routes.INVOICES) {
            InvoiceListScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
