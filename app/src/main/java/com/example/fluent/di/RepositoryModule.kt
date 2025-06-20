package com.example.fluent.di

import com.example.fluent.data.network.repository.AuthRepositoryImpl
import com.example.fluent.data.network.repository.ChatRepositoryImpl
import com.example.fluent.data.network.repository.WebSocketServiceImpl
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

    single<AuthRepository> {
        AuthRepositoryImpl(get((named("unauthenticated_client"))), get())
    }

    single<ChatRepository> {
        ChatRepositoryImpl(get((named("authenticated_client"))), get(), get(), get())
    }

    single<WebSocketService> {
        WebSocketServiceImpl(get(), get())
    }
}