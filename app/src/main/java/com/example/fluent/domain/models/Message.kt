package com.example.fluent.domain.models

data class Message(
    val isFromUser: Boolean,
    val content: String,
    val messageType: String,
    val attachmentID: Int? = null,
    val timestamp: String
)
