package com.example.fluent.presentation.message.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fluent.R
import com.example.fluent.presentation.common.CustomIconButton
import com.example.fluent.ui.theme.Peach
import com.example.fluent.ui.theme.PeachWhite

@Preview(showBackground = true)
@Composable
fun MessageTextField() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = PeachWhite,
        shape = RoundedCornerShape(30),
        shadowElevation = 10.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = "Hello World",
                onValueChange = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                    .clip(RoundedCornerShape(30)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = PeachWhite.copy(alpha = 0.1f),
                )
            )
            Spacer(modifier = Modifier.width(20.dp))
            CustomIconButton(
                onClick = {},
                icon = R.drawable.microphone,
                contentDescription = "Microphone",
                tint = Peach,
            )
            Spacer(modifier = Modifier.width(4.dp))
            CustomIconButton(
                onClick = {},
                icon = R.drawable.image_upload,
                contentDescription = "Image Upload",
                tint = Peach
            )
        }
    }
}