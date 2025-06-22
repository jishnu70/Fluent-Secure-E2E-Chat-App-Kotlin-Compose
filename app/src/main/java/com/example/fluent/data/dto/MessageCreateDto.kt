package com.example.fluent.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageCreateDto(
    val receiverID: Int,
    val content: String,
    val messageType: String,
//    val attachmentID: Int? = null
)
