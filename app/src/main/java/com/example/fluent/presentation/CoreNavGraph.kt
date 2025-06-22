package com.example.fluent.presentation

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.fluent.presentation.chatList.ChatListScreenRoot
import com.example.fluent.presentation.message.MessageScreenRoot
import com.example.fluent.presentation.userSearch.UserSearchScreenRoot
import com.example.fluent.ui.routes.AppScreenRoutes
import com.example.fluent.ui.routes.AuthScreenRoutes
import com.example.fluent.ui.routes.MainRoutes

fun NavGraphBuilder.coreAppNavGraph(
    navController: NavHostController,
    showMenuDrawer: MutableState<Boolean>
) {
    navigation(
        startDestination = AppScreenRoutes.ChatListScreen.route,
        route = MainRoutes.Core.route
    ) {
        composable(route = AppScreenRoutes.ChatListScreen.route) {
            ChatListScreenRoot(
                navController = navController,
                onChatClick = {
                    navController.navigate("${AppScreenRoutes.MessageScreen.route}/${it}")
                },
                onMenuClick = {
                    showMenuDrawer.value = true
                },
                onNonMenuClick = { route ->
                    showMenuDrawer.value = false
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                showMenuDrawer = showMenuDrawer.value,
                onShowMenuClose = { showMenuDrawer.value = it },
                onUserNotLoggedIn = {
                    navController.navigate(AuthScreenRoutes.LoginScreen.route)
                },
                onLogOutButtonClicked = {
                    navController.navigate(AuthScreenRoutes.LoginScreen.route)
                }
            )
        }

        composable(route = "${AppScreenRoutes.MessageScreen.route}/{chatID}") { backStackEntry ->
            val chatID = backStackEntry.arguments?.getString("chatID")?.toIntOrNull()
            MessageScreenRoot(
                chatId = chatID,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppScreenRoutes.UserSearchScreen.route) {
            UserSearchScreenRoot(
                navController = navController,
                onChatClick = {
                    navController.navigate("${AppScreenRoutes.MessageScreen.route}/${it}")
                },
                onMenuClick = {
                    showMenuDrawer.value = true
                },
                onNonMenuClick = { route ->
                    showMenuDrawer.value = false
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                showMenuDrawer = showMenuDrawer.value,
                onShowMenuClose = { showMenuDrawer.value = it },
                onUserNotLoggedIn = {
                    navController.navigate(AuthScreenRoutes.LoginScreen.route)
                },
                onLogOutButtonClicked = {
                    navController.navigate(AuthScreenRoutes.LoginScreen.route)
                }
            )
        }
    }
}