package com.example.fluent.presentation.message

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fluent.presentation.message.components.MessageTopBar

@Preview(showBackground = true)
@Composable
fun MessageScreenRoot() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MessageTopBar(modifier = Modifier.padding(vertical = 10.dp)) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 8.dp)
                .navigationBarsPadding()
        ) {
            MessageScreen()
        }
    }
}

@Composable
fun MessageScreen() {

}