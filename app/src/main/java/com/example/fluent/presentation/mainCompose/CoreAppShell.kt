package com.example.fluent.presentation.mainCompose

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.fluent.presentation.bottomNavigation.BottomNavigation
import com.example.fluent.presentation.chatList.ChatListAction
import com.example.fluent.presentation.chatList.components.ChatTopBarRoot
import com.example.fluent.presentation.coreAppNavGraph
import com.example.fluent.ui.routes.AppScreenRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoreAppShell(
    navController: NavHostController,
    showMenuDrawer: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            bottomBar = {
                BottomNavigation(
                    navController = navController,
                    showMenuDrawer = showMenuDrawer.value,
                    onMenuClick = { showMenuDrawer.value = true },
                    onNonMenuClick = { route ->
                        showMenuDrawer.value = false
                        navController.navigate(route)
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppScreenRoutes.ChatListScreen.route,
                modifier = modifier.padding(innerPadding)
            ) {
                coreAppNavGraph(navController, showMenuDrawer)
            }
        }
    }
}