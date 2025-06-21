package com.example.fluent.di

import com.example.fluent.presentation.authentication.AuthViewModel
import com.example.fluent.presentation.chatList.ChatListViewModel
import com.example.fluent.presentation.message.MessageViewModel
import com.example.fluent.presentation.profile.ProfileViewModel
import com.example.fluent.presentation.splashOpening.SplashScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel {
        AuthViewModel(
            authRepository = get(),
            keyManager = get()
        )
    }
    viewModel { SplashScreenViewModel(get()) }
    viewModel {
        ChatListViewModel(
            chatRepository = get(),
            tokenManager = get()
        )
    }
    viewModel { MessageViewModel(get()) }

    viewModel { ProfileViewModel(get()) }
}