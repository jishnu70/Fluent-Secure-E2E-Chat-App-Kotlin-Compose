package com.example.fluent.data.network.manager

import com.example.fluent.domain.models.Message

sealed interface WebsocketAction {
    data class OnConnected(val isConnected: Boolean) : WebsocketAction
    data class OnMessageReceived(val message: Message) : WebsocketAction
    data class OnError(val message: String) : WebsocketAction
    object OnDisconnected : WebsocketAction
}