package com.example.fluent.di

import com.example.fluent.data.remote.TokenManager
import com.example.fluent.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.koin.core.qualifier.named
import org.koin.dsl.module

val NetworkModule = module {
    single(named("unauthenticated_client")) {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }

    single(named("authenticated_client")) {
        val tokenManager: TokenManager = get()
        val authRepository: AuthRepository = get()

        HttpClient {
            install(ContentNegotiation) {
                json() // kotlinx.serialization
            }
            expectSuccess = false

            install(DefaultRequest) {
                tokenManager.getAccessToken()?.let {
                    header("Authorization", "Bearer $it")
                }
            }

            install(HttpRequestRetry) {
                retryIf { request, response ->
                    response.status == HttpStatusCode.Unauthorized
                }

                retryOnExceptionIf { request, cause ->
                    cause is ClientRequestException && cause.response.status == HttpStatusCode.Unauthorized
                }
                maxRetries = 1

                modifyRequest { request ->
                    val refreshResult = runBlocking {
                        try {
                            val refreshResult = authRepository.refreshToken()
                            if (refreshResult.isSuccess) {
                                val newAccessToken = tokenManager.getAccessToken()
                                if (!newAccessToken.isNullOrBlank()) {
                                    request.headers.remove("Authorization")
                                    request.header("Authorization", "Bearer $newAccessToken")
                                    refreshResult.getOrNull()
                                } else {
                                    tokenManager.clearTokens()
                                    throw Exception("Session expired: empty token.")
                                    null
                                }
                            } else {
                                tokenManager.clearTokens()
                                throw Exception("Session expired: refresh failed.")
                                null
                            }
                        } catch (e: Exception) {
                            tokenManager.clearTokens()
                            throw e
                            null
                        }
                    }
                    refreshResult?.let {
                        it.accessToken.let { token ->
                            request.headers.append(HttpHeaders.Authorization, "Bearer $token")
                        }
                    }
                }
            }
        }
    }
}