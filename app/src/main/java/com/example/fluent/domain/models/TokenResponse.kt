package com.example.fluent.domain.models

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)
