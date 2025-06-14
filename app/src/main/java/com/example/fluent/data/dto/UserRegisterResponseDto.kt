package com.example.fluent.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterResponseDto(
    @SerialName("id") val id: Int?,
    @SerialName("username") val username: String?,
    @SerialName("email") val email: String?
)
