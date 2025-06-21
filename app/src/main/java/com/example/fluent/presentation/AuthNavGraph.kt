package com.example.fluent.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.fluent.presentation.authentication.login.LoginScreenRoot
import com.example.fluent.presentation.authentication.register.SignUpScreenRoot
import com.example.fluent.ui.routes.AppScreenRoutes
import com.example.fluent.ui.routes.AuthScreenRoutes
import com.example.fluent.ui.routes.MainRoutes

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = AuthScreenRoutes.LoginScreen.route,
        route = MainRoutes.Auth.route
    ) {
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
    }
}