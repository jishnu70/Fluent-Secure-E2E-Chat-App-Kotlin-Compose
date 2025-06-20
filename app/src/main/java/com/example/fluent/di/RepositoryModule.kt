package com.example.fluent.di

import com.example.fluent.data.network.repository.AuthRepositoryImpl
import com.example.fluent.data.network.repository.ChatRepositoryImpl
import com.example.fluent.data.network.repository.WebSocketServiceImpl
import com.example.fluent.data.remote.EncryptionHelper
import com.example.fluent.data.remote.TokenManager
import com.example.fluent.domain.repository.AuthRepository
import com.example.fluent.domain.repository.ChatRepository
import com.example.fluent.domain.repository.WebSocketService
import com.example.fluent.domain.utility.KeyManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val RepositoryModule = module {

    single {
        TokenManager(androidContext())
    }

    single {
        KeyManager
    }

    single {
        EncryptionHelper()
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            httpClient = get((named("unauthenticated_client"))),
            tokenManager = get()
        )
    }

    single<WebSocketService> {
        WebSocketServiceImpl(get(), get())
    }

    single<ChatRepository> {
        ChatRepositoryImpl(
            httpClient = get((named("authenticated_client"))),
            tokenManager = get(),
            webSocketService = get(),
            encryptionHelper = get()
        )
    }
}