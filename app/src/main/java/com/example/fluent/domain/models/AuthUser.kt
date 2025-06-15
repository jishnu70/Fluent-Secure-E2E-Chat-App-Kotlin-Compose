package com.example.fluent.domain.models

data class AuthUser(
    val username: String,
    val email: String? = null,
    val password: String,
    val publicKey: String? = null
)
