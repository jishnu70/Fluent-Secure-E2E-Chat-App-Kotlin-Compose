package com.example.fluent.presentation.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.domain.models.MessageCreate
import com.example.fluent.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessageViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MessageState())
    val state = _state.asStateFlow()

    init {
        getMessages()
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
                        attachmentID = null
                    )

                    val partnerInfo = _state.value.partnerInfo
                    if (partnerInfo != null) {
                        val result = chatRepository.sendMessage(
                            message = newMessage,
                            partnerInfo = partnerInfo
                        )
                        if (result.isSuccess) {
                            _state.update {
                                it.copy(
                                    messageString = ""
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
        }
    }
}