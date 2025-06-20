package com.example.fluent.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginUserDto(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
)
