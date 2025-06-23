package com.example.fluent.presentation.message

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.data.mapper.toRSAPublicKey
import com.example.fluent.data.remote.EncryptionHelper
import com.example.fluent.domain.models.Message
import com.example.fluent.domain.models.MessageCreate
import com.example.fluent.domain.repository.ChatRepository
import com.example.fluent.domain.repository.WebSocketService
import com.example.fluent.domain.utility.KeyManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MessageViewModel(
    private val chatRepository: ChatRepository,
    private val webSocketService: WebSocketService,
    private val encryptionHelper: EncryptionHelper
) : ViewModel() {
    private val _state = MutableStateFlow(MessageState())
    val state = _state.asStateFlow()

    private var isCollectingSocketMessages = false
    private var currentChatId: Int? = null

    init {
        // ✅ Always listen to incoming messages once
        viewModelScope.launch {
            webSocketService.incomingMessages.collect { message ->
                val currentChatId = currentChatId ?: return@collect
                if (message.isFromUser || !message.isFromUser) {
                    _state.update { current ->
                        current.copy(
                            messages = listOf(message) + current.messages
                        )
                    }
                } else {
                    Log.d("MessageViewModel", "Message ignored (not for this chat)")
                }
            }
        }
    }

    fun initialize(chatId: Int) {
        currentChatId = chatId
        _state.update {
            it.copy(isLoading = true, messages = emptyList())
        }

        viewModelScope.launch {
            Log.d("MessageViewModel", "Initializing with chatId: $chatId")

            val partnerInfoResult = chatRepository.getPartnerInfo(chatId)
            if (partnerInfoResult.isSuccess) {
                val partnerInfo = partnerInfoResult.getOrNull()
                _state.update { it.copy(partnerInfo = partnerInfo) }
            }

            val connectResult = chatRepository.connectToChat(chatId)
            val messagesResult = chatRepository.getChatMessages(chatId)
            if (messagesResult.isSuccess) {
                val messages = messagesResult.getOrNull() ?: emptyList()
                _state.update {
                    it.copy(messages = messages, isLoading = false)
                }
            } else {
                _state.update {
                    it.copy(isLoading = false)
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

    @OptIn(ExperimentalTime::class)
    fun onAction(action: MessageAction) {
        when (action) {
            is MessageAction.OnSendMessage -> {
                val partnerInfo = _state.value.partnerInfo
                val messageText = action.message
                if (partnerInfo != null && messageText.isNotBlank()) {
                    val receiverPublicKey = partnerInfo.public_key.toRSAPublicKey()
                    val myPublicKey = KeyManager.getPublicKey().toRSAPublicKey()

                    val senderEncrypted = encryptionHelper.encrypt(messageText, myPublicKey)
                    val receiverEncrypted = encryptionHelper.encrypt(messageText, receiverPublicKey)

                    val newMessage = MessageCreate(
                        senderEncrypted = senderEncrypted,
                        receiverEncrypted = receiverEncrypted,
                        messageType = "text"
                    )

                    Log.d(
                        "MessageViewModel",
                        "Sending: $messageText to ${partnerInfo.user_name} (${partnerInfo.id})"
                    )
//                    val message = Message(
//                        isFromUser = true,
//                        content = messageText,
//                        messageType = "text",
//                        timestamp = Clock.System.now().toString()  // or use actual timestamp
//                    )
//
//                    _state.update {
//                        it.copy(
//                            messages = listOf(message) + it.messages,
//                            messageString = ""
//                        )
//                    }
                    val result = chatRepository.sendMessage(
                        message = newMessage,
                        partnerInfo = partnerInfo
                    )
                    if (result.isSuccess) {
                        _state.update {
                            it.copy(
                                messageString = "",
                                messages = it.messages
                            )
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