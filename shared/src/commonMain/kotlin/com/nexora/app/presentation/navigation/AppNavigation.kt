// AppNavigation.kt
package com.nexora.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexora.app.core.storage.TokenStorage
import com.nexora.app.data.repository.UserRepository
import com.nexora.app.presentation.cart.CartScreen
import com.nexora.app.presentation.home.HomeScreen
import com.nexora.app.presentation.home.HomeViewModel
import com.nexora.app.presentation.invoices.InvoiceListScreen
import com.nexora.app.presentation.login.LoginSelectionScreen
import com.nexora.app.presentation.login.OTPScreen
import com.nexora.app.presentation.login.email.EmailPasswordLoginScreen

object Routes {
    const val LOGIN_SELECTION = "login_selection"
    const val LOGIN_OTP = "login_otp"
    const val LOGIN_EMAIL = "login_email"
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
            Routes.LOGIN_SELECTION
        }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Login Selection Screen
        composable(Routes.LOGIN_SELECTION) {
            LoginSelectionScreen(
                onOtpLoginSelected = {
                    navController.navigate(Routes.LOGIN_OTP)
                },
                onEmailPasswordSelected = {
                    navController.navigate(Routes.LOGIN_EMAIL)
                }
            )
        }

        // OTP Login Screen
        composable(Routes.LOGIN_OTP) {
            OTPScreen(
                onBack = {
                    navController.popBackStack()
                },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN_SELECTION) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Email/Password Login Screen
        composable(Routes.LOGIN_EMAIL) {
            EmailPasswordLoginScreen(
                onBack = {
                    navController.popBackStack()
                },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN_SELECTION) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Home Screen
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
                    TokenStorage.clearToken()
                    navController.navigate(Routes.LOGIN_SELECTION) {
                        popUpTo(Routes.HOME) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Cart Screen
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

        // Invoices Screen
        composable(Routes.INVOICES) {
            InvoiceListScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}