package com.example.fluent.presentation.message.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fluent.R
import com.example.fluent.presentation.common.CustomIconButton
import com.example.fluent.presentation.message.MessageAction
import com.example.fluent.presentation.message.MessageState
import com.example.fluent.ui.theme.Brown
import com.example.fluent.ui.theme.Peach
import com.example.fluent.ui.theme.PeachWhite
import kotlinx.coroutines.delay

@Composable
fun MessageTextField(
    modifier: Modifier,
    state: MessageState,
    onAction: (MessageAction) -> Unit,
) {
    var textFieldSelected by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var showInsideButtons by remember { mutableStateOf(false) }
    var showOutsideButtons by remember { mutableStateOf(true) }

    LaunchedEffect(textFieldSelected) {
        if (textFieldSelected == true) {
            showOutsideButtons = false
            delay(300)
            showInsideButtons = true
        } else {
            showInsideButtons = false
            delay(300)
            showOutsideButtons = true
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(end = 8.dp)
            .clip(RoundedCornerShape(30))
            .background(Peach.copy(alpha = 0.4f), RoundedCornerShape(30)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .weight(3f)
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                .align(Alignment.CenterVertically),
            shape = RoundedCornerShape(30),
            color = PeachWhite,
            shadowElevation = 6.dp,
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .background(PeachWhite)
                    .animateContentSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = state.messageString,
                    onValueChange = { onAction(MessageAction.OnMessageChange(it)) },
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                        .onFocusChanged { focusSate ->
                            textFieldSelected = focusSate.isFocused
                        }
                        .focusRequester(focusRequester)
                        .align(Alignment.CenterVertically),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = PeachWhite,
                        focusedContainerColor = PeachWhite,
                        focusedTextColor = Brown,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    placeholder = { Text(text = "Message") },
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
                )
                AnimatedVisibility(
                    visible = showInsideButtons,
                    enter = fadeIn(tween(250)) + expandHorizontally(tween(100)),
                    exit = fadeOut(tween(250)) + shrinkHorizontally(tween(100)),
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomIconButton(
                            onClick = { onAction(MessageAction.OnSendMessage(state.messageString)) },
                            icon = R.drawable.arrow,
                            contentDescription = "Microphone",
                            tint = Peach,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        AnimatedVisibility(
            visible = showOutsideButtons,
            enter = fadeIn(tween(250)) + expandHorizontally(tween(100)),
            exit = fadeOut(tween(250)) + shrinkHorizontally(tween(100)),
            modifier = Modifier.weight(1f)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
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
}