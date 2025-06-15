package com.example.fluent.data.network.repository

import android.util.Log
import com.example.fluent.data.dto.MessageChatListDto
import com.example.fluent.data.dto.MessageResponseDto
import com.example.fluent.data.mapper.toDomainChatList
import com.example.fluent.data.mapper.toDomainMessage
import com.example.fluent.data.mapper.toRSAPublicKey
import com.example.fluent.data.network.MessageRoutes
import com.example.fluent.data.network.WebSocketRoutes
import com.example.fluent.data.remote.EncryptionHelper
import com.example.fluent.data.remote.TokenManager
import com.example.fluent.domain.models.ChatList
import com.example.fluent.domain.models.Message
import com.example.fluent.domain.models.MessageCreate
import com.example.fluent.domain.models.PartnerInfo
import com.example.fluent.domain.repository.ChatRepository
import com.example.fluent.domain.repository.WebSocketService
import com.example.fluent.domain.utility.KeyManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess

class ChatRepositoryImpl(
    private val httpClient: HttpClient,
    private val tokenManager: TokenManager,
    private val webSocketService: WebSocketService,
    private val encryptionHelper: EncryptionHelper
) : ChatRepository {
    override suspend fun fetchChats(): Result<List<ChatList>> {
        try {
            val result = httpClient.get(MessageRoutes.CHAT_LIST)
            Log.d("ChatList request sent: ", "Result: $result")
            if (result.status.isSuccess()) {
                val response = try {
                    result.body<List<MessageChatListDto>>()
                } catch (e: Exception) {
                    return Result.failure(Exception("Invalid server response", e))
                }
                Log.d("ChatList request received: ", "Response: $response")
                val chatList = try {
                    response.map { it.toDomainChatList() }
                } catch (e: Exception) {
                    return Result.failure(Exception("Invalid server response", e))
                }
                return Result.success(chatList)
            } else {
                return Result.failure(Exception("Failed to retrieve ChatList: ${result.status}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getChatMessages(receiverId: Int): Result<List<Message>> {
        try {
            val result = httpClient.get(MessageRoutes.GET_ALL_MESSAGES) {
                url {
                    parameters.append(name = "partnerID", value = receiverId.toString())
                }
            }
            Log.d("GetAllMessage request sent: ", "Result: $result")
            if (result.status.isSuccess()) {
                val response = try {
                    result.body<List<MessageResponseDto>>()
                } catch (e: Exception) {
                    return Result.failure(Exception("Invalid server response", e))
                }
                Log.d("GetAllMessage request received: ", "Response: $response")
                val messageList = try {
                    response.map {
                        val decrypted =
                            encryptionHelper.decrypt(it.content, KeyManager.getPrivateKey()!!)
                        it.copy(content = decrypted)
                            .toDomainMessage(receiverIDWebSocket = receiverId)
                    }
                } catch (e: Exception) {
                    return Result.failure(Exception("Invalid server response", e))
                }
                return Result.success(messageList)
            } else {
                return Result.failure(Exception("Failed to retrieve GetAllMessage: ${result.status}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun connectToChat(receiverId: Int): Result<Boolean> {
        try {
            if (tokenManager.hasAccessToken()) {
                val accessToken = try {
                    val response = tokenManager.getAccessToken()
                    response ?: throw Exception("Access token is null")
                } catch (e: Exception) {
                    Log.d("ChatRepositoryImpl", "Error getting access token: ${e.message}")
                    return Result.failure(e)
                }
                webSocketService.connect(
                    url = WebSocketRoutes.CHAT,
                    token = accessToken,
                    receiverId = receiverId
                )
                return Result.success(true)
            } else {
                Log.d("ChatRepositoryImpl", "There are no access token")
                return Result.failure(Exception("There are no access token"))
            }
        } catch (e: Exception) {
            Log.d("ChatRepositoryImpl", "Error connecting to chat: ${e.message}")
            return Result.failure(e)
        }
    }

    override fun sendMessage(message: MessageCreate, partnerInfo: PartnerInfo): Result<Boolean> {
        try {
            if (message.content.isNotBlank()) {
                val public_key_string = partnerInfo.public_key
                val public_key_encoding = try {
                    public_key_string.toRSAPublicKey()
                } catch (e: Exception) {
                    Log.d("ChatRepositoryImpl", "Error decoding public key: ${e.message}")
                    return Result.failure(e)
                }
                val encryptedMessage = encryptionHelper.encrypt(
                    message.content,
                    public_key_encoding
                )

                val newMessage = message.copy(content = encryptedMessage)
                webSocketService.send(message = newMessage)
                return Result.success(true)
            } else {
                return Result.failure(Exception("Message is empty"))
            }
        } catch (e: Exception) {
            Log.d("ChatRepositoryImpl", "Error sending message: ${e.message}")
            return Result.failure(e)
        }
    }

    override fun disconnect(): Result<Boolean> {
        try {
            webSocketService.disconnect()
            return Result.success(true)
        } catch (e: Exception) {
            Log.d("ChatRepositoryImpl", "Error disconnecting: ${e.message}")
            return Result.failure(e)
        }
    }
}