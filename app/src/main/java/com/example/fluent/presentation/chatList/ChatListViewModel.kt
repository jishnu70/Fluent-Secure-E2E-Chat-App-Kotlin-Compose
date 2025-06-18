package com.example.fluent.presentation.chatList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val chatRepository: ChatRepository
): ViewModel() {
    private val _state = MutableStateFlow(ChatListState())
    val state = _state.asStateFlow()

    init {
        getChatList()
    }

    fun getChatList() {
        viewModelScope.launch {
            val result = chatRepository.fetchChats()
            if (result.isSuccess) {
                _state.value = _state.value.copy(
                    chatList = result.getOrNull() ?: emptyList()
                )
            } else {
                _state.value = _state.value.copy(
                    error = result.exceptionOrNull()?.message ?: "Unknown error"
                )
            }
        }
    }

    fun onAction(action: ChatListAction) {
        when (action) {
            is ChatListAction.OnSearchQueryChange -> {
                _state.value = _state.value.copy(
                    searchQuery = action.query
                )
            }

            is ChatListAction.OnChatClick -> {
                _state.value = _state.value.copy(
                    selectedChatID = action.chatId
                )
            }
        }
    }
}