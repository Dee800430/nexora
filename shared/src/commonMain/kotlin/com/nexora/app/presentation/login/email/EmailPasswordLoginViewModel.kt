// EmailPasswordLoginViewModel.kt
package com.nexora.app.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexora.app.core.storage.TokenStorage
import com.nexora.app.data.model.user.LoginRequest
import com.nexora.app.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EmailPasswordUiState(
    val loginId: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false
)

class EmailPasswordLoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmailPasswordUiState())
    val uiState: StateFlow<EmailPasswordUiState> = _uiState.asStateFlow()

    fun onLoginIdChange(loginId: String) {
        _uiState.value = _uiState.value.copy(
            loginId = loginId,
            errorMessage = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            errorMessage = null
        )
    }

    fun login() {
        val currentState = _uiState.value

        // Validate inputs
        if (currentState.loginId.isEmpty()) {
            _uiState.value = currentState.copy(
                errorMessage = "Please enter your email or mobile number"
            )
            return
        }

        if (currentState.password.isEmpty()) {
            _uiState.value = currentState.copy(
                errorMessage = "Please enter your password"
            )
            return
        }

        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                // Using your existing UserRepository.loginUser()
                val request = LoginRequest(
                    loginId = currentState.loginId,
                    password = currentState.password
                )
                val response = userRepository.loginUser(request)

                // Save token
                TokenStorage.saveToken(response.token)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = false,
                    errorMessage = e.message ?: "Login failed. Please check your credentials."
                )
            }
        }
    }

    fun clearLoginSuccess() {
        _uiState.value = _uiState.value.copy(
            loginSuccess = false
        )
    }
}