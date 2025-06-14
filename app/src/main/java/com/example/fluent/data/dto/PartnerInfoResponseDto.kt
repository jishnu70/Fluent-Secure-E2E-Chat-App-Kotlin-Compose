package com.example.fluent.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PartnerInfoResponseDto(
    val id: Int,
    val user_name: String,
    val public_key: String
)
