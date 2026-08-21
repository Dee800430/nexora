package com.nexora.app.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexora.app.core.storage.TokenStorage
import com.nexora.app.data.model.user.LoginRequest
import com.nexora.app.data.api.UserApi
import com.nexora.app.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val loginId: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false
)

class LoginViewModel(private  val userRepository: UserRepository) : ViewModel() {


    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun onLoginIdChange(value: String) {
        _uiState.value = _uiState.value.copy(
            loginId = value,
            errorMessage = null
        )
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(
            password = value,
            errorMessage = null
        )
    }

    fun login() {


        val state = _uiState.value


        if (state.loginId.isBlank()) {


            _uiState.value = state.copy(
                errorMessage = "Please enter login ID"
            )

            return
        }

        if (state.password.isBlank()) {

            println("LOGIN: Password is blank")

            _uiState.value = state.copy(
                errorMessage = "Please enter password"
            )

            return
        }

        viewModelScope.launch {

            println("LOGIN: Coroutine started")

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {

                println("LOGIN: Creating LoginRequest")

                val request = LoginRequest(
                    loginId = state.loginId.trim(),
                    password = state.password
                )

                println("LOGIN: LoginRequest created")
                println("LOGIN: Calling UserApi.loginUser()")

                val response =userRepository.loginUser(request)

                TokenStorage.saveToken(response.token)

                println("LOGIN: Token saved")
                println("LOGIN: Token exists = ${TokenStorage.getToken() != null}")


                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true
                )


            } catch (e: Exception) {


                e.printStackTrace()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Login failed"
                )
            }

        }
    }

    fun clearLoginSuccess() {
        _uiState.value = _uiState.value.copy(
            loginSuccess = false
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}