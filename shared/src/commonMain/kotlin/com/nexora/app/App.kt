package com.nexora.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.nexora.app.presentation.navigation.AppNavigation

@Composable
fun App() {
    MaterialTheme {
        AppNavigation()
    }
}
