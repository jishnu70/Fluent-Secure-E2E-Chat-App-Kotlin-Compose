package com.example.fluent.presentation.authentication

data class AuthState(
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val registerNewUser: Boolean = false,
    val isLoading: Boolean = false,
    val username: String = "",
    val email: String? = null,
    val password: String = "",
    val confirmPassword: String? = null,
    val isPasswordSame: Boolean = false
)
