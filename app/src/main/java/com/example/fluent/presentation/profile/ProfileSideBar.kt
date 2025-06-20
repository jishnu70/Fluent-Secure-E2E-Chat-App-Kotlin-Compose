package com.example.fluent.presentation.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun ProfileSideBar(modifier: Modifier, showMenuDrawer: Boolean) {
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
                Text("Profile", modifier = Modifier.clickable { /* Action */ })
                Text("Settings", modifier = Modifier.clickable { /* Action */ })
                Text("Logout", modifier = Modifier.clickable { /* Action */ })
            }
        }
    }
}