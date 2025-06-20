package com.example.fluent.presentation.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fluent.presentation.message.components.MessageDisplay
import com.example.fluent.presentation.message.components.MessageTextField
import com.example.fluent.presentation.message.components.MessageTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun MessageScreenRoot(
    viewModel: MessageViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
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
        topBar = {
            MessageTopBar(
                modifier = Modifier.padding(vertical = 10.dp),
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 4.dp)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }

                state.error != null -> {
                    Text(text = state.error)
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                    ) {
                        MessageScreen(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            state = state
                        )
                        MessageTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            state = state,
                            onAction = {action ->
                                when(action) {
                                    is MessageAction.OnSendMessage -> {
                                        viewModel.onAction(action = action)
                                    }

                                    is MessageAction.OnMessageChange -> {
                                        viewModel.onAction(action = action)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageScreen(
    modifier: Modifier = Modifier,
    state: MessageState,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        reverseLayout = true,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.messages) { message ->
            MessageDisplay(
                message = message
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