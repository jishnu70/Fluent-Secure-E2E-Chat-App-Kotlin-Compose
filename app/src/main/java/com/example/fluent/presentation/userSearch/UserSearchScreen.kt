package com.example.fluent.presentation.userSearch

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun UserSearchScreenRoot(
    navController: NavController,
    onChatClick: (Int) -> Unit,
    onMenuClick: () -> Unit,
    onNonMenuClick: (String) -> Unit,
    showMenuDrawer: Boolean,
    onShowMenuClose: (Boolean) -> Unit,
    onUserNotLoggedIn: () -> Unit,
    onLogOutButtonClicked: () -> Unit
) {

}

@Composable
fun UserSearchScreen() {

}