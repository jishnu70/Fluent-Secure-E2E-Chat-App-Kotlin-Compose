package com.example.fluent.domain.repository

import com.example.fluent.domain.models.TokenResponse
import kotlinx.coroutines.flow.Flow

interface TokenStorageRepository {
    fun getAccessToken(): Flow<String>
    fun getRefreshToken(): Flow<String>
    suspend fun saveTokens(tokens: TokenResponse)
    suspend fun clearTokens()
}