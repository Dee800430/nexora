package com.nexora.app.core.storage

object TokenStorage {

    private var token: String? = null

    fun saveToken(token: String) {
        this.token = token
    }

    fun getToken(): String? {
        return token
    }

    fun clearToken() {
        token = null
    }

    fun isLoggedIn(): Boolean {
        return !token.isNullOrBlank()
    }

}