package com.example.fluent.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageChatListDto(
    val partner: PartnerInfoResponseDto,
    val message: MessageResponseDto
)
