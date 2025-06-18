package com.example.fluent.ui.routes

sealed class AuthScreenRoutes(val route: String) {
    object LoginScreen : AuthScreenRoutes("loginScreen")
    object RegisterScreen : AuthScreenRoutes("registerScreen")
}