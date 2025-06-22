package com.example.fluent.presentation.userSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.data.remote.TokenManager
import com.example.fluent.domain.models.PartnerInfo
import com.example.fluent.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserSearchViewModel(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
): ViewModel() {
    private val _state = MutableStateFlow(UserSearchState())
    val state = _state.asStateFlow()

    init {
        checkLoggedIn()
        if (_state.value.isLoggedId == true) {
            getAllUser()
        }
    }

    fun checkLoggedIn() {
        val result = tokenManager.isLoggedIn()
        _state.update {
            it.copy(isLoggedId = result)
        }
    }

    fun getAllUser() {
        viewModelScope.launch {
            val result = authRepository.getAllUser()
            if (result.isSuccess) {
                _state.value = _state.value.copy(
                    userList = result.getOrDefault(defaultValue = emptyList<PartnerInfo>())
                )
            } else {
                _state.value = _state.value.copy(
                    error = result.exceptionOrNull()?.message ?: "Unknown error"
                )
            }
        }
    }

    fun onAction(action: UserSearchAction) {
        when (action) {
            is UserSearchAction.OnChatClick -> {
                _state.value = _state.value.copy(
                    selectedChatID = action.chatId
                )
            }
            UserSearchAction.OnSearch -> {
                viewModelScope.launch {
                    val result = authRepository.getAllUser(userName = _state.value.searchQuery)
                    if (result.isSuccess) {
                        _state.value = _state.value.copy(
                            userList = result.getOrDefault(defaultValue = emptyList<PartnerInfo>())
                        )
                    } else {
                        _state.value = _state.value.copy(
                            error = result.exceptionOrNull()?.message ?: "Unknown error"
                        )
                    }
                }
            }
            is UserSearchAction.OnSearchQueryChange -> {
                _state.value = _state.value.copy(
                    searchQuery = action.query
                )
            }
        }
    }
}