package com.example.fluent.data.mapper

import com.example.fluent.data.dto.MessageCreateDto
import com.example.fluent.data.dto.MessageResponseDto
import com.example.fluent.domain.models.Message
import com.example.fluent.domain.models.MessageCreate

fun MessageResponseDto.toDomainMessage(receiverIDWebSocket: Int): Message {
    return Message(
        isFromUser = (receiverID == receiverIDWebSocket),
        content = content,
        messageType = messageType,
        attachmentID = attachmentID,
        timestamp = timestamp
    )
}

fun MessageCreate.toDataMessage(receiverIDWebSocket: Int): MessageCreateDto {
    return MessageCreateDto(
        receiverID = receiverIDWebSocket,
        content = content,
        messageType = messageType,
        attachmentID = attachmentID,
    )
}