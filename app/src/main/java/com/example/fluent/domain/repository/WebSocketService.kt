package com.example.fluent.domain.repository

import com.example.fluent.domain.models.Message
import com.example.fluent.domain.models.MessageCreate
import kotlinx.coroutines.flow.Flow

interface WebSocketService {
    fun connect(url: String, token: String, receiverId: Int)
    fun disconnect()
    fun send(message: MessageCreate)
    val incomingMessages: Flow<Message>
}