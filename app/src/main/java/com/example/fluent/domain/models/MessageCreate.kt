package com.example.fluent.domain.models

data class MessageCreate(
    val content: String,
    val messageType: String,
//    val attachmentID: Int? = null
)
