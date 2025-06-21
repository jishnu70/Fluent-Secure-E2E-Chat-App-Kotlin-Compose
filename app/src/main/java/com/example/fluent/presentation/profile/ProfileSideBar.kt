package com.example.fluent.presentation.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fluent.R
import com.example.fluent.ui.theme.Peach
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSideBar(
    viewModel: ProfileViewModel = koinViewModel(),
    modifier: Modifier,
    showMenuDrawer: Boolean,
    onLogOutButtonClicked: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    AnimatedVisibility(
        visible = showMenuDrawer,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it }),
        modifier = modifier
            .clip(RoundedCornerShape(15))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 40.dp, bottom = 80.dp, end = 10.dp)
                .clip(RoundedCornerShape(15))
                .width(350.dp)
                .background(Color.White)
//                    .shadow(2.dp)
                .pointerInput(Unit) { detectTapGestures(onTap = {}) }
        ) {
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                    }

                    state.error != null -> {
                        Text(text = state.error)
                    }

                    else -> {
                        Box(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .size(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.user_circle),
                                contentDescription = "User Profile Icon",
                                colorFilter = ColorFilter.tint(Peach),
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Profile",
                            modifier = Modifier.clickable { /* Action */ },
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Text(
                            "Logout", modifier = Modifier.clickable {
                                viewModel.onAction(
                                    ProfileAction.OnLogout
                                )
                                onLogOutButtonClicked()
                            },
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
//                Text("Settings", modifier = Modifier.clickable { /* Action */ })
            }
        }
    }
}