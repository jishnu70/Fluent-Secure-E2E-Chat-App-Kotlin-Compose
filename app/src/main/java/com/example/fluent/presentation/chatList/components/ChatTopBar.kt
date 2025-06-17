package com.example.fluent.presentation.chatList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.fluent.R
import com.example.fluent.ui.theme.MatteWhiteGrayMedium
import com.example.fluent.ui.theme.Orange
import com.example.fluent.ui.theme.Peach
import com.example.fluent.ui.theme.PeachWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBarRoot(scrollBehavior: TopAppBarScrollBehavior) {

    Surface(
        tonalElevation = 12.dp,
        shadowElevation = 10.dp,
        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp),
    ) {
        TopAppBar(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)),
            title = {
                Column(
                    modifier = Modifier.padding(bottom = 10.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            Text(
                                text = "Fluent ",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.W900,
                                color = Orange,
                                textDecoration = TextDecoration.Underline
                            )
                            Text(
                                text = "Chat",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.W400,
                                color = Color.Black,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    MatteWhiteGrayMedium,
                                    RoundedCornerShape(45)
                                )
                                .shadow(
                                    elevation = 10.dp,
                                    RoundedCornerShape(45),
                                    ambientColor = Peach,
                                    spotColor = PeachWhite
                                )
                                .padding(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.new_message),
                                contentDescription = "New Message",
                                tint = Color.Unspecified,
                            )
                        }
                    }
                    ChatSearchBar()
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = PeachWhite),
            scrollBehavior = scrollBehavior,
            expandedHeight = TopAppBarDefaults.MediumAppBarExpandedHeight
        )
    }
}