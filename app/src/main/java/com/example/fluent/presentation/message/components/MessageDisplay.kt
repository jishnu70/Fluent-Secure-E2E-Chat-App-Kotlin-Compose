package com.example.fluent.presentation.message.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fluent.presentation.message.MessageDummy
import com.example.fluent.ui.theme.Peach

@Composable
fun MessageDisplay(
    message: MessageDummy
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .align(if (message.isFromUser) Alignment.End else Alignment.Start)
                .clip(
                    RoundedCornerShape(
                        topStart = 48f,
                        topEnd = 48f,
                        bottomStart = if (message.isFromUser) 48f else 0f,
                        bottomEnd = if (message.isFromUser) 0f else 48f
                    )
                )
                .background(if (message.isFromUser)Peach else Peach.copy(alpha = 0.4f))
                .padding(16.dp)
        ) {
            Text(text = "Hello from ${message.message}")
        }
    }
}