package com.example.fluent.ui.routes

sealed class AppScreenRoutes(val route: String) {
    object SplashScreen : AppScreenRoutes("splashScreen")
    object ChatListScreen : AppScreenRoutes("chatListScreen")
    object MessageScreen : AppScreenRoutes("messageScreen")
    object UserSearchScreen : AppScreenRoutes("userSearchScreen")
    object ProfileScreen : AppScreenRoutes("profileScreen")
}