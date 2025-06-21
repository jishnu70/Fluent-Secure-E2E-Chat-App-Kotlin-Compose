package com.example.fluent.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.fluent.presentation.authentication.login.LoginScreenRoot
import com.example.fluent.presentation.authentication.register.SignUpScreenRoot
import com.example.fluent.presentation.chatList.ChatListScreenRoot
import com.example.fluent.presentation.message.MessageScreenRoot
import com.example.fluent.presentation.splashOpening.SplashScreenRoot
import com.example.fluent.ui.routes.AppScreenRoutes
import com.example.fluent.ui.routes.AuthScreenRoutes
import com.example.fluent.ui.routes.MainRoutes

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

        authGraph(navController = navController)
        coreAppNavGraph(navController = navController, showMenuDrawer = showMenuDrawer)
    }
}