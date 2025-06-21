package com.example.fluent.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.fluent.data.remote.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(
    val tokenManager: TokenManager
): ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    fun onAction(action: ProfileAction) {
        when(action) {
            ProfileAction.OnLogout -> {
                Log.d("ProfileViewModel", "Logout")
                tokenManager.clearTokens()
            }
        }
    }
}