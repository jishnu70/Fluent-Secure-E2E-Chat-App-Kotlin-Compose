package com.example.fluent.presentation.authentication

sealed interface AuthAction {
    data class OnUserNameChange(val newUserName: String) : AuthAction
    data class OnEmailChange(val newEmail: String? = null) : AuthAction
    data class OnPasswordChange(val newPassword: String) : AuthAction
    data class OnConfirmPasswordChange(val confirmPassword: String?): AuthAction
    data class OnRegisterNewUser(val register: Boolean): AuthAction
    object OnSubmit : AuthAction
    object OnToggleRegisterLoginMode : AuthAction
}