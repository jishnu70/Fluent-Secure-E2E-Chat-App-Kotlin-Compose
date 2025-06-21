package com.example.fluent.presentation.profile

sealed interface ProfileAction {
    object OnLogout: ProfileAction
}