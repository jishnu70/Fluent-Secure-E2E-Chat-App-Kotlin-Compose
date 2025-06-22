package com.example.fluent.presentation.userSearch


sealed interface UserSearchAction {
    data class OnChatClick(val chatId: Int) : UserSearchAction
    data class OnSearchQueryChange(val query: String) : UserSearchAction
    object OnSearch : UserSearchAction
}