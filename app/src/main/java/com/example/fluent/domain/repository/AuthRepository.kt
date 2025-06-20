package com.example.fluent.domain.repository

import com.example.fluent.domain.models.LoginUser
import com.example.fluent.domain.models.RegisterUser
import com.example.fluent.domain.models.TokenResponse

interface AuthRepository {
    suspend fun registerNewUser(newUser: RegisterUser): Result<TokenResponse>
    suspend fun loginUser(user: LoginUser): Result<TokenResponse>
    suspend fun loginAfterRegister(username: String, password: String): Result<TokenResponse>
    suspend fun checkUserLogin(): Boolean
    suspend fun refreshToken(): Result<TokenResponse>
}