package com.nexora.app.core.network.storage

class ApiException(
    val statusCode: Int,
    val statusDescription: String,
    override val message: String
) : Exception(message)