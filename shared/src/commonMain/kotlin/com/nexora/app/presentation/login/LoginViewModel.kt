// LoginViewModel.kt
package com.nexora.app.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexora.app.core.storage.TokenStorage
import com.nexora.app.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val mobile: String = "",
    val otp: String = "",
    val isLoading: Boolean = false,
    val isOtpSent: Boolean = false,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val userId: Long = 0,
    val token: String = ""
)

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Update mobile number
    fun onMobileChange(value: String) {
        _uiState.update {
            it.copy(
                mobile = value,
                errorMessage = null
            )
        }
    }

    // Update OTP
    fun onOtpChange(value: String) {
        _uiState.update {
            it.copy(
                otp = value.take(6), // Limit to 6 digits
                errorMessage = null
            )
        }
    }

    // Step 1: Send OTP
    fun sendOtp() {
        val mobile = _uiState.value.mobile.trim()

        // Validate mobile number
        if (mobile.isEmpty() || mobile.length < 10) {
            _uiState.update {
                it.copy(
                    errorMessage = "Please enter a valid 10-digit mobile number",
                    isLoading = false
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                isOtpSent = false
            )
        }

        viewModelScope.launch {
            try {
                println("📱 Sending OTP to: $mobile")

                val response = userRepository.sendOtp(mobile)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isOtpSent = true,
                        successMessage = "OTP sent successfully to $mobile",
                        errorMessage = null
                    )
                }

                println("✅ OTP sent successfully: $response")

            } catch (e: Exception) {
                println("❌ Failed to send OTP: ${e.message}")
                e.printStackTrace()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isOtpSent = false,
                        errorMessage = "Failed to send OTP: ${e.message}"
                    )
                }
            }
        }
    }

    // Step 2: Verify OTP and Login
    fun verifyOtp() {
        val mobile = _uiState.value.mobile.trim()
        val otp = _uiState.value.otp.trim()

        // Validate mobile number
        if (mobile.isEmpty() || mobile.length < 10) {
            _uiState.update {
                it.copy(
                    errorMessage = "Please enter a valid mobile number",
                    isLoading = false
                )
            }
            return
        }

        // Validate OTP
        if (otp.isEmpty() || otp.length != 6) {
            _uiState.update {
                it.copy(
                    errorMessage = "Please enter a valid 6-digit OTP",
                    isLoading = false
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            try {
                println("🔐 Verifying OTP for: $mobile")

                val response = userRepository.loginWithOtp(mobile, otp)

                // Save token
                TokenStorage.saveToken(response.token)

                println("✅ Login successful!")

                println("✅ Token: ${response.token}")

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = true,

                        token = response.token,
                        errorMessage = null
                    )
                }

            } catch (e: Exception) {
                println("❌ Login failed: ${e.message}")
                e.printStackTrace()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = false,
                        errorMessage = "Login failed: ${e.message}"
                    )
                }
            }
        }
    }

    // Resend OTP
    fun resendOtp() {
        _uiState.update {
            it.copy(
                isOtpSent = false,
                otp = "",
                errorMessage = null,
                successMessage = null
            )
        }
        // Send OTP again
        sendOtp()
    }

    // Go back to mobile entry
    fun goBackToMobile() {
        _uiState.update {
            it.copy(
                isOtpSent = false,
                otp = "",
                errorMessage = null,
                successMessage = null
            )
        }
    }

    // Clear login success state
    fun clearLoginSuccess() {
        _uiState.update {
            it.copy(loginSuccess = false)
        }
    }

    // Clear error
    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}