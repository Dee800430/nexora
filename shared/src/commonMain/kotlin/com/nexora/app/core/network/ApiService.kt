package com.nexora.app.core.network

object ApiService {

    fun baseUrl(service: String): String {
        return when (service.lowercase()) {
            "item" -> ApiConfig.ITEM_SERVICE_URL
            "user" -> ApiConfig.USER_SERVICE_URL
            "organization" -> ApiConfig.ORGANIZATION_SERVICE_URL
            "order" -> ApiConfig.INVOICE_SERVICE_URL

            else -> throw IllegalArgumentException(
                "Unknown service: $service"
            )
        }
    }

    suspend inline fun <reified T> request(
        service: String,
        endpoint: String,
        method: String = "GET",

    ): T {

        return ApiClient.request<T>(
            url = "${baseUrl(service)}$endpoint",
            method = method,

        )

    }
    suspend inline fun <reified T, reified B> request(
        service: String,
        endpoint: String,
        method: String,
        body: B
    ): T {

        return ApiClient.request<T,B>(
            url = "${baseUrl(service)}$endpoint",
            method = method,
            body = body
        )
    }
}