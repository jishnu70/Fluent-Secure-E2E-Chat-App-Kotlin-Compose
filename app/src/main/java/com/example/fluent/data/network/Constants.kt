package com.example.fluent.data.network

const val BASE_URL = "https://cryptalkfastapi.onrender.com"

object AuthRoutes {
    const val REGISTER = "$BASE_URL/auth/register"
    const val LOGIN = "$BASE_URL/auth/login"
    const val REFRESH_TOKEN = "$BASE_URL/auth/refresh"
    const val GET_ALL_USER = "$BASE_URL/auth/get_all_users"
}

object MessageRoutes {
    const val PARTNER_INFO = "$BASE_URL/chat/partnerinfo"
    const val CHAT_LIST = "$BASE_URL/chat/chat_list"
    const val GET_ALL_MESSAGES = "$BASE_URL/chat/all_messages"
}

object WebSocketRoutes {
    const val CHAT = "wss://cryptalkfastapi.onrender.com/chat/ws"
}