package com.example.fluent.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageCreateDto(
    val receiverID: Int,
    val receiver_encrypted: String,
    val sender_encrypted: String,
    val messageType: String,
)
