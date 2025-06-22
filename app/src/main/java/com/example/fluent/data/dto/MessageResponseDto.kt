package com.example.fluent.data.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class MessageResponseDto(
    @SerialName("sender_id") val senderID: Int,
    @SerialName("receiver_id") val receiverID: Int,
    @SerialName("content") val content: String,
    @SerialName("message_type") val messageType: String,
    @SerialName("timestamp") val timestamp: String
)
