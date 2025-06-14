package com.example.fluent.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponseDto(
    val senderID: Int,
    val receiverID: Int,
    val content: String,
    val messageType: String,
    val attachmentID: Int? = null,
    val timestamp: String
)
