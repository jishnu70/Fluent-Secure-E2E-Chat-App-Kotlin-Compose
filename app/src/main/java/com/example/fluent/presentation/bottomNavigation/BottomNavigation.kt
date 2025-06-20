package com.example.fluent.presentation.bottomNavigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fluent.ui.routes.AppScreenRoutes
import com.example.fluent.ui.theme.Peach

@Composable
fun BottomNavigation(
    navController: NavController,
    showMenuDrawer: Boolean,
    onMenuClick: () -> Unit,
    onNonMenuClick: (String) -> Unit
) {
    val screens = listOf(
        BottomNavigationItems.Home,
        BottomNavigationItems.UserSearchScreen,
        BottomNavigationItems.Menu,
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .navigationBarsPadding()
            .height(50.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        screens.forEach { screen ->
            BarItem(
                screen = screen,
                currentDestination = currentDestination,
                onMenuClick = onMenuClick,
                showMenuDrawer = showMenuDrawer,
                onNonMenuClick = onNonMenuClick
            )
        }
    }
}

@Composable
fun BarItem(
    screen: BottomNavigationItems,
    currentDestination: NavDestination?,
    showMenuDrawer: Boolean,
    onMenuClick: () -> Unit,
    onNonMenuClick: (String) -> Unit
) {
    val selected = when {
        screen.route == AppScreenRoutes.ProfileScreen.route -> showMenuDrawer
        else -> !showMenuDrawer && screen.route == currentDestination?.route
    }

    val borderColor = if (selected) Peach else Color.Gray

    Box(
        modifier = Modifier
            .height(70.dp)
            .width(100.dp)
            .clip(CircleShape)
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (screen.route == AppScreenRoutes.ProfileScreen.route) {
                            onMenuClick()
                        } else {
                            onNonMenuClick(screen.route)
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp).height(80.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(screen.icon),
                contentDescription = screen.title,
                tint = borderColor,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterHorizontally)
            )
//            Spacer(modifier = Modifier.height(2.dp))
            AnimatedVisibility(visible = selected) {
                Text(
                    text = screen.title,
                    color = borderColor,
//                    modifier = Modifier.padding(top = 2.dp),
                    style = MaterialTheme.typography.titleSmall
                )
            }
//            if (!selected) {
//                Spacer(modifier = Modifier.height(18.dp)) // Match Text height
//            }
        }
    }
}