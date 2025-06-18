package com.example.fluent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.fluent.presentation.AppNavigationGraph
import com.example.fluent.ui.theme.FluentTheme
import com.example.fluent.ui.theme.PeachWhite

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FluentTheme {
                Scaffold(modifier = Modifier.fillMaxSize().background(PeachWhite)) { innerPadding ->
                    AppNavigationGraph(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}