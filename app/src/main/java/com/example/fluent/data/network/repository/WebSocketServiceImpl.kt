package com.example.fluent.data.network.repository

import android.util.Log
import com.example.fluent.data.dto.MessageCreateDto
import com.example.fluent.data.dto.MessageResponseDto
import com.example.fluent.data.mapper.toDataMessage
import com.example.fluent.data.mapper.toDomainMessage
import com.example.fluent.data.remote.EncryptionHelper
import com.example.fluent.domain.models.Message
import com.example.fluent.domain.models.MessageCreate
import com.example.fluent.domain.repository.WebSocketService
import com.example.fluent.domain.utility.KeyManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class WebSocketServiceImpl(
    private val httpClient: HttpClient,
    private val encryptionHelper: EncryptionHelper
) : WebSocketService {
    private var session: WebSocketSession? = null
    private val _incomingMessages = MutableSharedFlow<Message>()


    private var _receiverId: Int? = null

    override fun connect(url: String, token: String, receiverId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("TOKEN_PHYSICAL", token)
                if (token.isBlank()) {
                    Log.e("WebSocketServiceImpl", "Cannot connect: token is blank")
                    return@launch
                }
                val fullUrl = "$url?token=$token"
                session = httpClient.webSocketSession {
                    url(fullUrl)
                }
                _receiverId = receiverId
                Log.d("WebSocketServiceImpl", "Connected to $url")
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
                            Log.d("Incoming encrypted", "📨 Incoming encrypted: ${msg.content}")
                            Log.d(
                                "Private key available",
                                "🔑 Private key available: ${KeyManager.getPrivateKey() != null}"
                            )
                            Log.d("Private key", "🔑 Private key: ${KeyManager.getPrivateKey()}")
                            Log.d("msg", msg.toString())
                            val decrypted = try {
                                encryptionHelper.decrypt(
                                    msg.content,
                                    KeyManager.getPrivateKey()
                                        ?: throw Exception("Private key not found")
                                )
                            } catch (e: Exception) {
                                Log.d(
                                    "WebSocketServiceImpl",
                                    "Error decrypting message: ${e.message}"
                                )
                                return@launch
                            }
                            msg = msg.copy(content = decrypted)
                            _incomingMessages.emit(
                                msg.toDomainMessage(
                                    _receiverId
                                        ?: throw IllegalStateException("Receiver ID not set")
                                )
                            )
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

    override val incomingMessages = _incomingMessages.asSharedFlow()
}