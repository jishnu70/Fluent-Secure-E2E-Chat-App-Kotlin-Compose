package com.example.fluent.presentation.chatList.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fluent.R
import com.example.fluent.ui.theme.Brown
import com.example.fluent.ui.theme.Orange
import com.example.fluent.ui.theme.Peach
import com.example.fluent.ui.theme.PeachWhite

@Composable
fun ChatSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onImeSearch: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = Orange,
            backgroundColor = Peach.copy(alpha = 0.4f)
        )
    ) {
        OutlinedTextField(
            value = searchQuery,
            textStyle = TextStyle(fontSize = 16.sp),
            onValueChange = { onSearchQueryChange(it) },
            shape = RoundedCornerShape(50),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Orange,
                focusedBorderColor = Orange,
                focusedTextColor = Brown,
                unfocusedContainerColor = PeachWhite.copy(alpha = 0.15f),
                unfocusedBorderColor = Brown,
            ),
            placeholder = { Text(text = stringResource(R.string.search_hint)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = if (isFocused) {
                        Orange.copy(alpha = 0.66f)
                    } else {
                        Color.Black.copy(alpha=0.5f)
                    }
                )
            },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onImeSearch()
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            trailingIcon = {
                AnimatedVisibility(
                    visible = searchQuery.isNotBlank()
                ) {
                    IconButton(
                        onClick = { onSearchQueryChange("") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.close_hint),
                            tint = Brown
                        )
                    }
                }
            },
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .background(
                    shape = RoundedCornerShape(50),
                    color = PeachWhite,
                )
                .minimumInteractiveComponentSize()
        )
    }
}