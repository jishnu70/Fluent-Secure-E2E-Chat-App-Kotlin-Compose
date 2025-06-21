package com.example.fluent.presentation.mainCompose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.fluent.presentation.authGraph
import com.example.fluent.ui.routes.AuthScreenRoutes

@Composable
fun AuthNavShell(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AuthScreenRoutes.LoginScreen.route,
        modifier = modifier
    ) {
        authGraph(navController)
    }
}