package com.nexora.app.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit = {},
) {
    val factory = androidx.compose.runtime.remember {
        LoginViewModelFactory(
            userRepository = com.nexora.app.data.repository.UserRepository()
        )
    }
    val viewModel: LoginViewModel = viewModel(
        factory = factory
    )
    val uiState by viewModel.uiState.collectAsState()

    /*
     * Navigate after successful login
     */
    LaunchedEffect(uiState.loginSuccess) {

        if (uiState.loginSuccess) {

            println("NAVIGATION: Login successful")
            println("NAVIGATION: Going to Home")

            viewModel.clearLoginSuccess()

            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Login to continue",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        /*
         * Login ID
         */
        OutlinedTextField(
            value = uiState.loginId,
            onValueChange = viewModel::onLoginIdChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Login ID")
            },
            singleLine = true,
            enabled = !uiState.isLoading
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        /*
         * Password
         */
        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Password")
            },
            singleLine = true,
            enabled = !uiState.isLoading,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        /*
         * Forgot password
         */
        TextButton(
            onClick = onForgotPassword,
            enabled = !uiState.isLoading,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Forgot Password?")
        }

        /*
         * Error
         */
        uiState.errorMessage?.let { message ->

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        /*
         * Login button
         */
        Button(
            onClick = viewModel::login,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !uiState.isLoading
        ) {

            if (uiState.isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier.height(22.dp),
                    strokeWidth = 2.dp
                )

            } else {

                Text("Login")
            }
        }
    }
}