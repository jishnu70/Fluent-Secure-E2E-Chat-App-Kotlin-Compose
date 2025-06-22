package com.example.fluent.data.network.repository

import android.util.Log
import com.example.fluent.data.dto.PartnerInfoResponseDto
import com.example.fluent.data.dto.TokenResponseDto
import com.example.fluent.data.dto.UserRegisterResponseDto
import com.example.fluent.data.mapper.toDomainPartnerInfo
import com.example.fluent.data.mapper.toDomainTokenResponse
import com.example.fluent.data.mapper.toDtoLogin
import com.example.fluent.data.mapper.toDtoRegister
import com.example.fluent.data.network.AuthRoutes
import com.example.fluent.data.remote.TokenManager
import com.example.fluent.domain.models.LoginUser
import com.example.fluent.domain.models.PartnerInfo
import com.example.fluent.domain.models.RegisterUser
import com.example.fluent.domain.models.TokenResponse
import com.example.fluent.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun registerNewUser(newUser: RegisterUser): Result<TokenResponse> {
        try {
            Log.d("RegisterDebug", "Sending register request: ${newUser.toDtoRegister()}")
            val result = httpClient.post(AuthRoutes.REGISTER) {
                setBody(newUser.toDtoRegister())
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
            Log.e("RegisterNewUser", "Exception: ${e.localizedMessage}", e)
            return Result.failure(e)
        }
    }

    override suspend fun loginUser(user: LoginUser): Result<TokenResponse> {
        return try {
            val result = httpClient.post(AuthRoutes.LOGIN) {
                contentType(ContentType.Application.Json)
                setBody(user.toDtoLogin())
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
            Log.e("LoginError", "Exception during login: ${e.localizedMessage}", e)
            Result.failure(e)
        }
    }

    override suspend fun loginAfterRegister(
        username: String,
        password: String
    ): Result<TokenResponse> =
        loginUser(user = LoginUser(username = username, password = password))

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

    override suspend fun getAllUser(userName: String): Result<List<PartnerInfo>> {
        try {
            val result = httpClient.get(AuthRoutes.GET_ALL_USER) {
                url {
                    parameters.append("search", userName)
                }
            }
            if (result.status.isSuccess()) {
                val response = try {
                    result.body<List<PartnerInfoResponseDto>>()
                } catch (e: Exception) {
                    return Result.failure(Exception("Invalid server response", e))
                }
                if (response.isNotEmpty()) {
                    val partnerInfo = response.map { it.toDomainPartnerInfo() }
                    return Result.success(partnerInfo)
                } else {
                    return Result.failure(Exception("Failed to get all users: ${result.status}"))
                }
            } else {
                return Result.failure(Exception("Failed to get all users: ${result.status}"))
            }
        } catch (e: Exception) {
            Log.d("getAllUser", "Exception: ${e.localizedMessage}")
            return Result.failure(e)
        }
    }

    private fun saveJWTTokens(tokens: TokenResponseDto) {
        tokens.let {
            tokenManager.saveTokens(tokens = it)
        }
    }
}