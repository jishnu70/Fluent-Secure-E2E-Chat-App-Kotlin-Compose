package com.example.fluent.presentation.chatList

import com.example.fluent.domain.models.ChatList

data class ChatListState(
    val isLoggedId: Boolean = false,
    val error: String? = null,
    val chatList: List<ChatList> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val selectedChatID: Int? = null
)
