package com.example.fluent.presentation.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fluent.presentation.message.components.MessageDisplay
import com.example.fluent.presentation.message.components.MessageTextField
import com.example.fluent.presentation.message.components.MessageTopBar

@Preview(showBackground = true)
@Composable
fun MessageScreenRoot() {
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        topBar = { MessageTopBar(modifier = Modifier.padding(vertical = 10.dp)) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 8.dp)
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            ) {
                MessageScreen(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                MessageTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun MessageScreen(modifier: Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        reverseLayout = true,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(data) { item ->
            MessageDisplay(
                message = item
            )
        }
    }
}

val data = (1..100).map {
    MessageDummy(
        id = it,
        message = "User $it",
        isFromUser = it % 2 == 0
    )
}

data class MessageDummy(
    val id: Int,
    val message: String,
    val isFromUser: Boolean
)