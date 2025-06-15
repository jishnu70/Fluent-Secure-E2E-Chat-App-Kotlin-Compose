package com.example.fluent.data.mapper

import com.example.fluent.data.dto.MessageChatListDto
import com.example.fluent.data.dto.MessageCreateDto
import com.example.fluent.data.dto.MessageResponseDto
import com.example.fluent.data.dto.PartnerInfoResponseDto
import com.example.fluent.domain.models.ChatList
import com.example.fluent.domain.models.Message
import com.example.fluent.domain.models.MessageCreate
import com.example.fluent.domain.models.PartnerInfo

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

fun PartnerInfoResponseDto.toDomainPartnerInfo(): PartnerInfo {
    return PartnerInfo(
        id = id,
        user_name = user_name,
        public_key = public_key
    )
}

fun MessageChatListDto.toDomainChatList(): ChatList {
    return ChatList(
        partner = partner.toDomainPartnerInfo(),
        message = message.toDomainMessage(partner.id)
    )
}