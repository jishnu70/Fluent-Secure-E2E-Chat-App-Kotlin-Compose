package com.example.fluent.presentation.message

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.domain.models.Message
import com.example.fluent.domain.models.MessageCreate
import com.example.fluent.domain.models.PartnerInfo
import com.example.fluent.domain.repository.ChatRepository
import com.example.fluent.domain.repository.WebSocketService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessageViewModel(
    private val chatRepository: ChatRepository,
    private val webSocketService: WebSocketService
) : ViewModel() {
    private val _state = MutableStateFlow(MessageState())
    val state = _state.asStateFlow()

    private var isCollectingSocketMessages = false

    fun initialize(chatId: Int) {
        viewModelScope.launch {
            Log.d("MessageViewModel", "Initializing with chatId: $chatId")
            _state.update { it.copy(isLoading = true) }

            val partnerInfoResult = chatRepository.getPartnerInfo(chatId)
            val connectResult = chatRepository.connectToChat(chatId)
            val messagesResult = chatRepository.getChatMessages(chatId)

            if (messagesResult.isFailure) {
                Log.d("MessageViewModel", "Error fetching messages: ${messagesResult.exceptionOrNull()?.message ?: "unknown error"}")
            }

            if (partnerInfoResult.isSuccess && connectResult.isSuccess && messagesResult.isSuccess) {
                _state.update {
                    it.copy(
                        partnerInfo = partnerInfoResult.getOrElse {
                            throw it
                        }, // Ideally fetch name & key too
                        messages = messagesResult.getOrDefault(emptyList()),
                        isLoading = false
                    )
                }
                launch {
                    webSocketService.incomingMessages.collect { incomingMessage ->
                        _state.update { currentState ->
                            currentState.copy(
                                messages = listOf(incomingMessage) + currentState.messages
                            )
                        }
                    }
                }
            } else {
                _state.update {
                    it.copy(
                        error = connectResult.exceptionOrNull()?.message
                            ?: messagesResult.exceptionOrNull()?.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun getMessages() {
        viewModelScope.launch {
            val receiverID = _state.value.partnerInfo?.id
            if (receiverID != null) {
                val result = chatRepository.getChatMessages(receiverID)

                if (result.isSuccess) {
                    _state.update {
                        it.copy(
                            messages = result.getOrDefault(emptyList())
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            error = result.exceptionOrNull()?.message
                        )
                    }
                }
            } else {
                _state.update {
                    it.copy(
                        error = "Receiver ID is null"
                    )
                }
            }
        }
    }

    fun onAction(action: MessageAction) {
        when (action) {
            is MessageAction.OnSendMessage -> {
                val message = action.message
                if (message.isNotBlank()) {
                    val newMessage = MessageCreate(
                        content = message,
                        messageType = "text",
//                        attachmentID = null
                    )

                    val partnerInfo = _state.value.partnerInfo
                    if (partnerInfo != null) {
                        Log.d(
                            "MessageViewModel",
                            "Sending: $message to ${partnerInfo.user_name} (${partnerInfo.id})"
                        )
                        val result = chatRepository.sendMessage(
                            message = newMessage,
                            partnerInfo = partnerInfo
                        )
                        if (result.isSuccess) {
                            val myOwnMessage = Message(
                                isFromUser = true,
                                content = newMessage.content,
                                messageType = newMessage.messageType,
                                timestamp = ""
                            )
                            _state.update {
                                it.copy(
                                    messageString = "",
                                    messages = it.messages + myOwnMessage
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    error = result.exceptionOrNull()?.message
                                )
                            }
                        }
                    } else {
                        _state.update {
                            it.copy(
                                error = "Receiver ID is null"
                            )
                        }
                    }
                } else {
                    _state.update {
                        it.copy(
                            error = "Message cannot be empty"
                        )
                    }
                }
            }

            is MessageAction.OnMessageChange -> {
                _state.update {
                    it.copy(
                        messageString = action.message
                    )
                }
            }

            MessageAction.OnDisconnect -> {
                viewModelScope.launch {
                    val result = chatRepository.disconnect()
                    if (result.isSuccess) {
                        _state.update {
                            it.copy(
                                partnerInfo = null
                            )
                        }
                    }
                }
            }
        }
    }
}