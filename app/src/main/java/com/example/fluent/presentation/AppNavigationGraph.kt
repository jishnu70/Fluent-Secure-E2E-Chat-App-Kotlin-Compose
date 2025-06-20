package com.example.fluent.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fluent.presentation.authentication.login.LoginScreenRoot
import com.example.fluent.presentation.authentication.register.SignUpScreenRoot
import com.example.fluent.presentation.chatList.ChatListScreenRoot
import com.example.fluent.presentation.message.MessageScreenRoot
import com.example.fluent.presentation.splashOpening.SplashScreenRoot
import com.example.fluent.ui.routes.AppScreenRoutes
import com.example.fluent.ui.routes.AuthScreenRoutes

@Composable
fun AppNavigationGraph(
    modifier: Modifier,
) {
    val navController = rememberNavController()

    val showMenuDrawer = remember { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = AppScreenRoutes.SplashScreen.route) {
        composable(route = AppScreenRoutes.SplashScreen.route) {
            SplashScreenRoot(
                modifier = modifier,
                onLoggedIn = {
                    navController.navigate(AppScreenRoutes.ChatListScreen.route) {
                        popUpTo(AppScreenRoutes.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onLoggedOut = {
                    navController.navigate(AuthScreenRoutes.LoginScreen.route) {
                        popUpTo(AppScreenRoutes.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable(route = AuthScreenRoutes.LoginScreen.route) {
            LoginScreenRoot(
                onRegisterButtonClicked = {
                    navController.navigate(AuthScreenRoutes.RegisterScreen.route)
                },
                onBackClick = { navController.popBackStack() },
                onLoginSuccess = {
                    navController.navigate(AppScreenRoutes.ChatListScreen.route)
                }
            )
        }

        composable(route = AuthScreenRoutes.RegisterScreen.route) {
            SignUpScreenRoot(
                onLoginButtonClicked = {
                    navController.navigate(AuthScreenRoutes.LoginScreen.route)
                },
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(AppScreenRoutes.ChatListScreen.route)
                },
            )
        }

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
                onShowMenuClose = { showMenuDrawer.value = it }
            )
        }

        composable(route = "${AppScreenRoutes.MessageScreen.route}/{chatID}") {
            MessageScreenRoot(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppScreenRoutes.UserSearchScreen.route) {

        }
    }
}