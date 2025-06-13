package com.example.fluent.presentation.chatList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fluent.presentation.chatList.components.ChatListItem
import com.example.fluent.presentation.chatList.components.ChatTopBarRoot

@Preview(showBackground = true)
@Composable
fun ChatListScreenRoot() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize() ,
        topBar = { ChatTopBarRoot() },
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 8.dp)
                .navigationBarsPadding()
        ) {
            ChatList()
        }
    }
}

@Composable
private fun ChatList() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(count = 20) {
            ChatListItem()
        }
    }
}