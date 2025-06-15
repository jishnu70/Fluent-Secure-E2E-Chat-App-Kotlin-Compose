package com.example.fluent.data.network.repository

import android.util.Log
import com.example.fluent.data.dto.MessageCreateDto
import com.example.fluent.data.dto.MessageResponseDto
import com.example.fluent.data.mapper.toDataMessage
import com.example.fluent.data.mapper.toDomainMessage
import com.example.fluent.domain.models.Message
import com.example.fluent.domain.models.MessageCreate
import com.example.fluent.domain.repository.WebSocketService
import com.example.fluent.data.remote.EncryptionHelper
import com.example.fluent.domain.utility.KeyManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class WebSocketServiceImpl(
    private val httpClient: HttpClient,
    private val encryptionHelper: EncryptionHelper
) : WebSocketService {
    private var session: WebSocketSession? = null
    private val _incomingMessages = MutableSharedFlow<MessageResponseDto>()


    private var _receiverId: Int? = null

    override fun connect(url: String, token: String, receiverId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                session = httpClient.webSocketSession {
                    url(url)
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
                _receiverId = receiverId
                listenForIncomingMessages()
            } catch (e: Exception) {
                println("WebSocket connection failed: ${e.message}")
            }
        }
    }

    private fun listenForIncomingMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                session?.incoming?.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val rawJson = frame.readText()
                        try {
                            var msg = Json.decodeFromString<MessageResponseDto>(rawJson)
                            val decrypted =
                                encryptionHelper.decrypt(msg.content, KeyManager.getPrivateKey()!!)
                            msg = msg.copy(content = decrypted)
                            _incomingMessages.emit(msg)
                        } catch (e: Exception) {
                            println("Failed to parse message: $rawJson → ${e.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error receiving message: ${e.message}")
            }
        }
    }

    override fun disconnect() {
        CoroutineScope(Dispatchers.IO).launch {
            session?.close()
            session = null
        }
    }

    override fun send(message: MessageCreate) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val messageDto = message.toDataMessage(
                    _receiverId ?: throw IllegalStateException("Receiver ID not set")
                )
                val messageJson = Json.encodeToString(MessageCreateDto.serializer(), messageDto)
                session?.send(Frame.Text(messageJson))
            } catch (e: Exception) {
                Log.d("WebSocketServiceImpl", "Error sending message: ${e.message}")
            }
        }
    }

    override val incomingMessages: Flow<Message>
        get() = _incomingMessages.map {
            it.toDomainMessage(
                _receiverId ?: throw IllegalStateException("Receiver ID not set")
            )
        }
}