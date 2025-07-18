package com.example.fluent.presentation.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.domain.models.LoginUser
import com.example.fluent.domain.models.RegisterUser
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
                    Log.d("AuthViewModel", "onAction: Submitting")
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                    runCatching {
                        if (_state.value.registerNewUser) {
                            val getPublicKey = keyManager.getPublicKey()
                            val user = RegisterUser(
                                username = _state.value.username,
                                email = _state.value.username,
                                password = _state.value.password,
                                publicKey = getPublicKey
                            )
                            if (_state.value.password.contentEquals( _state.value.confirmPassword)){
                                validateEmailOrNull(user.email)?.let { error ->
                                    _state.update { it.copy(error = error) }
                                    return@launch
                                }
                                Log.d("AuthViewModel", "onAction: Registering new user")
                                Log.d("AuthViewModel", "onAction: User: $user")
                                authRepository.registerNewUser(user)
                            } else {
                                _state.update {
                                    it.copy(error = "Passwords do not match")
                                }
                                Log.d("AuthViewModel", "onAction: Passwords do not match")
                                return@launch
                            }
                        } else {
                            val user = LoginUser(
                                username = _state.value.username,
                                password = _state.value.password
                            )
                            Log.d("AuthViewModel", "onAction: User: $user")
                            Log.d("AuthViewModel", "onAction: Logging in user")
                            authRepository.loginUser(user)
                        }
                    }.onSuccess { result ->
                        if (result.isSuccess) {
                            _state.value = _state.value.copy(
                                isAuthenticated = true,
                                error = null
                            )
                            Log.d("AuthViewModel", "onAction: Success")
                        } else {
                            _state.value = _state.value.copy(
                                error = result.exceptionOrNull()?.message ?: "Unknown error"
                            )
                            Log.d("AuthViewModel", "onAction: Error")
                        }
                    }.onFailure {
                        Log.d("AuthViewModel", "${it.localizedMessage}")
                        _state.value = _state.value.copy(error = it.message)
                        Log.d("AuthViewModel", "onAction: Failure")
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