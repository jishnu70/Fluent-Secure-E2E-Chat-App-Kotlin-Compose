package com.example.fluent.data.network.repository

import android.util.Log
import com.example.fluent.data.dto.TokenResponseDto
import com.example.fluent.data.dto.UserRegisterResponseDto
import com.example.fluent.data.mapper.toDomainTokenResponse
import com.example.fluent.data.mapper.toDtoUser
import com.example.fluent.data.network.AuthRoutes
import com.example.fluent.domain.models.AuthUser
import com.example.fluent.domain.models.TokenResponse
import com.example.fluent.domain.repository.AuthRepository
import com.example.fluent.data.remote.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun registerNewUser(newUser: AuthUser): Result<TokenResponse> {
        try {
            val result = httpClient.post(AuthRoutes.REGISTER) {
                setBody(newUser.toDtoUser())
            }
            Log.d("Register request sent: ", "Result: $result")
            if (result.status.isSuccess()) {
                val response = try {
                    result.body<UserRegisterResponseDto>()
                } catch (e: Exception) {
                    return Result.failure(Exception("Invalid server response", e))
                }
                Log.d("Register request received: ", "Response: $response")
                if (response.id != null) {
                    val loginCall = loginAfterRegister(
                        username = response.username
                            ?: throw IllegalStateException("username cannot be null"),
                        password = newUser.password
                    )
                    Log.d("Login call result", ": $loginCall")
                    return loginCall
                } else {
                    return Result.failure(Exception("Failed to register user: ${result.status}"))
                }
            } else {
                return Result.failure(Exception("Failed to register user: ${result.status}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun loginUser(user: AuthUser): Result<TokenResponse> {
        return try {
            val result = httpClient.post(AuthRoutes.LOGIN) {
                setBody(user)
            }
            Log.d("Login Request sent", "Result: $result")
            if (result.status.isSuccess()) {
                val response = try {
                    result.body<TokenResponseDto>()
                } catch (e: Exception) {
                    return Result.failure(Exception("Invalid server response", e))
                }
                Log.d("Login request received", "Response: $response")
                if (response.accessToken != null) {
                    saveJWTTokens(response)
                    return Result.success(response.toDomainTokenResponse())
                } else {
                    return Result.failure(Exception("Login failed: HTTP ${result.status}"))
                }
            } else {
                Result.failure(Exception("Login failed: HTTP ${result.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginAfterRegister(
        username: String,
        password: String
    ): Result<TokenResponse> =
        loginUser(user = AuthUser(username = username, password = password))

    override suspend fun checkUserLogin(): Boolean {
        return tokenManager.isLoggedIn()
    }

    override suspend fun refreshToken(): Result<TokenResponse> {
        return try {
            val refreshToken = tokenManager.getRefreshToken()
                ?: return Result.failure(Exception("No refresh token available"))

            val response = httpClient.post(AuthRoutes.REFRESH_TOKEN) {
                setBody(mapOf("refresh_token" to refreshToken))
            }

            if (response.status.isSuccess()) {
                val tokenDto = try {
                    response.body<TokenResponseDto>()
                } catch (e: Exception) {
                    return Result.failure(Exception("Invalid server response", e))
                }
                tokenManager.saveTokens(tokenDto)
                Result.success(tokenDto.toDomainTokenResponse())
            } else {
                Result.failure(Exception("Failed to refresh token"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun saveJWTTokens(tokens: TokenResponseDto) {
        tokens.let {
            tokenManager.saveTokens(tokens = it)
        }
    }
}