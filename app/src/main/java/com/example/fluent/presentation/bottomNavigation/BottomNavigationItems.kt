package com.example.fluent.presentation.bottomNavigation

import com.example.fluent.R
import com.example.fluent.ui.routes.AppScreenRoutes

sealed class BottomNavigationItems(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Home: BottomNavigationItems(
        route = AppScreenRoutes.ChatListScreen.route,
        title = "ChatListScreen",
        icon = R.drawable.conversation
    )

    object UserSearchScreen: BottomNavigationItems(
        route = AppScreenRoutes.UserSearchScreen.route,
        title = "UserSearchScreen",
        icon = R.drawable.search_profile_14886154
    )

    object Menu: BottomNavigationItems(
        route = AppScreenRoutes.ProfileScreen.route,
        title = "ProfileScreen",
        icon = R.drawable.menu
    )
}