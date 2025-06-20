package com.example.fluent.presentation.splashOpening

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.data.remote.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val tokenManager: TokenManager
): ViewModel() {
    private val _state = MutableStateFlow(SplashScreenState())
    val state = _state.asStateFlow()

    init {
        checkUserLogIn()
    }

    fun checkUserLogIn() {
        viewModelScope.launch {
            val isLoggedIn = tokenManager.isLoggedIn()
            _state.update {
                it.copy(isLoggedIn = isLoggedIn)
            }
        }
    }
}