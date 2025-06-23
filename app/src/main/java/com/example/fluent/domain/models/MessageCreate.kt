package com.example.fluent.domain.models

data class MessageCreate(
    val senderEncrypted: String,
    val receiverEncrypted: String,
    val messageType: String,
)
