package com.example.fluent.ui.routes

sealed class MainRoutes(val route: String) {
    object Auth: MainRoutes("auth")
    object Core : AuthScreenRoutes("core")
}