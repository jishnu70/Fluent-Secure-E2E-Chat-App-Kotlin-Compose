package com.example.fluent.presentation.chatList

sealed interface ChatListAction {
    data class OnChatClick(val chatId: Int) : ChatListAction
    data class OnSearchQueryChange(val query: String) : ChatListAction
    object OnSearch : ChatListAction
}