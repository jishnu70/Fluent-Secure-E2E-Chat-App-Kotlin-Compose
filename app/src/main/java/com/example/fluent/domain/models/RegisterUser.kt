package com.example.fluent.domain.models

data class RegisterUser(
    val username: String,
    val email: String,
    val password: String,
    val publicKey: String?
)
