package com.example.fluent.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.domain.models.AuthUser
import com.example.fluent.domain.repository.AuthRepository
import com.example.fluent.domain.utility.Email
import com.example.fluent.domain.utility.KeyManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val keyManager: KeyManager
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    init {
        checkLoggedIn()
    }

    fun checkLoggedIn() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isAuthenticated = authRepository.checkUserLogin()
            )
        }
    }

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnUserNameChange -> {
                _state.value = _state.value.copy(
                    username = action.newUserName
                )
            }

            is AuthAction.OnEmailChange -> {
                _state.value = _state.value.copy(
                    email = if (action.newEmail.isNullOrBlank()) null else action.newEmail
                )
            }

            is AuthAction.OnPasswordChange -> {
                _state.value = _state.value.copy(
                    password = action.newPassword
                )
            }

            is AuthAction.OnSubmit -> {
                viewModelScope.launch {
                    val user = AuthUser(
                        username = _state.value.username,
                        email = _state.value.email,
                        password = _state.value.password
                    )
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                    runCatching {
                        if (_state.value.registerNewUser) {
                            val getPublicKey = keyManager.getPublicKey()
                            val userWithPublicKey = user.copy(publicKey = getPublicKey)
                            if (_state.value.password.contentEquals( _state.value.confirmPassword)){
                                validateEmailOrNull(_state.value.email)?.let { error ->
                                    _state.update { it.copy(error = error) }
                                    return@launch
                                }
                                authRepository.registerNewUser(userWithPublicKey)
                            } else {
                                _state.update {
                                    it.copy(error = "Passwords do not match")
                                }
                                return@launch
                            }
                        } else {
                            authRepository.loginUser(user)
                        }
                    }.onSuccess { result ->
                        if (result.isSuccess) {
                            _state.value = _state.value.copy(
                                isAuthenticated = true,
                                error = null
                            )
                        } else {
                            _state.value = _state.value.copy(
                                error = result.exceptionOrNull()?.message ?: "Unknown error"
                            )
                        }
                    }.onFailure {
                        _state.value = _state.value.copy(error = it.message)
                    }
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                }
            }

            AuthAction.OnToggleRegisterLoginMode -> {
                _state.value = _state.value.copy(
                    registerNewUser = !_state.value.registerNewUser
                )
            }

            is AuthAction.OnConfirmPasswordChange -> {
                _state.value = _state.value.copy(
                    confirmPassword = action.confirmPassword
                )
            }

            is AuthAction.OnRegisterNewUser -> {
                _state.update {
                    it.copy(
                        registerNewUser = action.register
                    )
                }
            }
        }
    }

    private fun validateEmailOrNull(email: String?): String? {
        return try {
            Email(email ?: throw IllegalArgumentException("Email cannot be null"))
            null
        } catch (e: Exception) {
            e.message
        }
    }
}