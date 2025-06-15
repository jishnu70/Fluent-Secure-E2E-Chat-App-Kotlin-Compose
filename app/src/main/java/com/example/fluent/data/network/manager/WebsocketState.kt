package com.example.fluent.data.network.manager

import com.example.fluent.domain.models.Message

data class WebsocketState(
    val isConnected: Boolean = false,
    val messages: List<Message> = emptyList(),
    val errorMessage: String? = null
)
