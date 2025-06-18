package com.example.fluent.presentation.message

sealed interface MessageAction {
    data class OnMessageChange(val message: String): MessageAction
    data class OnSendMessage(val message: String): MessageAction
}