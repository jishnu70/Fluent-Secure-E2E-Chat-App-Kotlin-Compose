package com.example.fluent.domain.repository

import com.example.fluent.domain.models.Message

interface ChatRepository {
    suspend fun fetchChats(): List<Message>
    suspend fun connectToChat(receiverId: Int)
    fun sendMessage(message: String, senderId: Int)
}