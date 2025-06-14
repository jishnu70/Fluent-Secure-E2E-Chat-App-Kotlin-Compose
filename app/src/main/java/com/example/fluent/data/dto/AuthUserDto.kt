package com.example.fluent.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthUserDto(
    @SerialName("username") val username: String,
    @SerialName("email") val email: String? = null,
    @SerialName("password") val password: String
)
