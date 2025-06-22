package com.example.fluent.presentation.chatList.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.fluent.R
import com.example.fluent.domain.models.ChatList
import com.example.fluent.ui.theme.Brown
import com.example.fluent.ui.theme.Peach
import com.example.fluent.ui.theme.PeachWhite
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimestamp(timestamp: String): String {
    val instant = Instant.parse(timestamp)
    val zoneId = ZoneId.systemDefault()
    val localDateTime = instant.atZone(zoneId).toLocalDateTime()
    val messageDate = localDateTime.toLocalDate()
    val currentDate = LocalDate.now(zoneId)

    return when {
        messageDate == currentDate -> localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        messageDate == currentDate.minusDays(1) -> "Yesterday"
        else -> localDateTime.format(DateTimeFormatter.ofPattern("dd MMM"))
    }
}

@Composable
fun ChatListItem(
    chat: ChatList,
    onChatClick: (ChatList) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .clickable(onClick = { onChatClick(chat) }),
        color = PeachWhite.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(50.dp)
                    .background(color = Peach, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person",
                    tint = Brown,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                        .background(Color.Green.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            }
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(modifier = Modifier.size(20.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.thumbtacks),
                            contentDescription = "pin to favorites",
                            tint = Peach.copy(alpha = 0.81f)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = chat.partner.user_name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Brown,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = formatTimestamp(chat.message.timestamp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Brown,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .align(Alignment.Top)
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = chat.message.content,
                    style = MaterialTheme.typography.labelLarge,
                    color = Brown.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}