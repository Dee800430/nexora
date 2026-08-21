package com.nexora.app.core.network

import com.nexora.app.core.network.storage.ApiException
import com.nexora.app.core.storage.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {

    // ---------------------------------------------------------
    // HTTP CLIENT
    // ---------------------------------------------------------

    val client = HttpClient {

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                    explicitNulls = false
                }
            )
        }

        install(Logging) {

            logger = object : Logger {

                override fun log(message: String) {
                    println("KTOR: $message")
                }
            }

            level = LogLevel.ALL
        }
    }

    // ---------------------------------------------------------
    // AUTHORIZATION
    // ---------------------------------------------------------

    fun HttpRequestBuilder.addAuthorization() {

        TokenStorage.getToken()?.let { token ->

            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )
        }
    }

    // ---------------------------------------------------------
    // GET
    // ---------------------------------------------------------

    suspend inline fun <reified T> get(
        url: String
    ): T {

        val response = client.get(url) {
            addAuthorization()
        }

        return handleResponse(response)
    }

    // ---------------------------------------------------------
    // POST WITH BODY
    // ---------------------------------------------------------

    suspend inline fun <reified T, reified B> post(
        url: String,
        body: B
    ): T {

        val response = client.post(url) {

            addAuthorization()

            contentType(ContentType.Application.Json)

            // IMPORTANT:
            // B is the real type, e.g. LoginRequest.
            // It is no longer erased to Any.
            setBody(body)
        }

        return handleResponse(response)
    }

    // ---------------------------------------------------------
    // POST WITHOUT BODY
    // ---------------------------------------------------------

    suspend inline fun <reified T> post(
        url: String
    ): T {

        val response = client.post(url) {
            addAuthorization()
        }

        return handleResponse(response)
    }

    // ---------------------------------------------------------
    // PUT WITH BODY
    // ---------------------------------------------------------

    suspend inline fun <reified T, reified B> put(
        url: String,
        body: B
    ): T {

        val response = client.put(url) {

            addAuthorization()

            contentType(ContentType.Application.Json)

            setBody(body)
        }

        return handleResponse(response)
    }

    // ---------------------------------------------------------
    // PUT WITHOUT BODY
    // ---------------------------------------------------------

    suspend inline fun <reified T> put(
        url: String
    ): T {

        val response = client.put(url) {
            addAuthorization()
        }

        return handleResponse(response)
    }

    // ---------------------------------------------------------
    // PATCH WITH BODY
    // ---------------------------------------------------------

    suspend inline fun <reified T, reified B> patch(
        url: String,
        body: B
    ): T {

        val response = client.patch(url) {

            addAuthorization()

            contentType(ContentType.Application.Json)

            setBody(body)
        }

        return handleResponse(response)
    }

    // ---------------------------------------------------------
    // PATCH WITHOUT BODY
    // ---------------------------------------------------------

    suspend inline fun <reified T> patch(
        url: String
    ): T {

        val response = client.patch(url) {
            addAuthorization()
        }

        return handleResponse(response)
    }

    // ---------------------------------------------------------
// GENERIC REQUEST WITH BODY
// ---------------------------------------------------------

    suspend inline fun <reified T, reified B> request(
        url: String,
        method: String,
        body: B
    ): T {

        println("API REQUEST ----------------------")
        println("URL: $url")
        println("METHOD: $method")
        println("BODY: $body")
        println("BODY TYPE: ${B::class}")
        println("TOKEN EXISTS: ${TokenStorage.isLoggedIn()}")

        val response = when (method.uppercase()) {

            "POST" -> client.post(url) {

                addAuthorization()

                contentType(ContentType.Application.Json)

                setBody(body)
            }

            "PUT" -> client.put(url) {

                addAuthorization()

                contentType(ContentType.Application.Json)

                setBody(body)
            }

            "PATCH" -> client.patch(url) {

                addAuthorization()

                contentType(ContentType.Application.Json)

                setBody(body)
            }

            else -> throw IllegalArgumentException(
                "Body is not supported for method: $method"
            )
        }

        println("API RESPONSE --------------------")
        println("STATUS: ${response.status}")

        return handleResponse(response)
    }

    // ---------------------------------------------------------
// GENERIC REQUEST WITHOUT BODY
// ---------------------------------------------------------

    suspend inline fun <reified T> request(
        url: String,
        method: String = "GET"
    ): T {

        println("API REQUEST ----------------------")
        println("URL: $url")
        println("METHOD: $method")
        println("BODY: null")
        println("TOKEN EXISTS: ${TokenStorage.isLoggedIn()}")

        val response = when (method.uppercase()) {

            "GET" -> client.get(url) {
                addAuthorization()
            }

            "DELETE" -> client.delete(url) {
                addAuthorization()
            }

            "POST" -> client.post(url) {
                addAuthorization()
            }

            "PUT" -> client.put(url) {
                addAuthorization()
            }

            "PATCH" -> client.patch(url) {
                addAuthorization()
            }

            else -> throw IllegalArgumentException(
                "Unsupported HTTP method: $method"
            )
        }

        println("API RESPONSE --------------------")
        println("STATUS: ${response.status}")

        return handleResponse(response)
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------

    suspend fun delete(
        url: String
    ): HttpResponse {

        val response = client.delete(url) {
            addAuthorization()
        }

        if (!response.status.isSuccess()) {

            throw ApiException(
                statusCode = response.status.value,
                statusDescription = response.status.description,
                message =
                    "API error: ${response.status.value} ${response.status.description}"
            )
        }

        return response
    }

    // ---------------------------------------------------------
    // RESPONSE HANDLER
    // ---------------------------------------------------------

    suspend inline fun <reified T> handleResponse(
        response: HttpResponse
    ): T {

        if (!response.status.isSuccess()) {

            throw ApiException(
                statusCode = response.status.value,
                statusDescription = response.status.description,
                message =
                    "API error: ${response.status.value} ${response.status.description}"
            )
        }

        // 204 No Content
        if (response.status.value == 204) {

            @Suppress("UNCHECKED_CAST")
            return Unit as T
        }

        return response.body()
    }
}