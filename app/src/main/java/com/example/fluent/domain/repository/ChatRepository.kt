package com.example.fluent.domain.repository

import com.example.fluent.domain.models.ChatList
import com.example.fluent.domain.models.Message
import com.example.fluent.domain.models.MessageCreate
import com.example.fluent.domain.models.PartnerInfo

interface ChatRepository {
    suspend fun fetchChats(): Result<List<ChatList>>
    suspend fun getChatMessages(receiverId: Int): Result<List<Message>>
    suspend fun connectToChat(receiverId: Int): Result<Boolean>
    suspend fun getPartnerInfo(receiverId: Int): Result<PartnerInfo>
    fun sendMessage(message: MessageCreate, partnerInfo: PartnerInfo): Result<Boolean>
    fun disconnect(): Result<Boolean>
}