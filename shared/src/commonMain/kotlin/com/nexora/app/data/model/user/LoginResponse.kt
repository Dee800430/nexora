package com.nexora.app.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
)