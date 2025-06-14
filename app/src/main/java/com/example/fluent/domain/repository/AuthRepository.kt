package com.example.fluent.domain.repository

import com.example.fluent.domain.models.AuthUser
import com.example.fluent.domain.models.TokenResponse

interface AuthRepository {
    suspend fun registerNewUser(newUser: AuthUser): Result<TokenResponse>
    suspend fun loginUser(user: AuthUser): Result<TokenResponse>
    suspend fun loginAfterRegister(username: String, password: String): Result<TokenResponse>
    suspend fun checkUserLogin(): Boolean
    suspend fun refreshToken(): Result<TokenResponse>
}