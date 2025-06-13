package com.example.fluent.presentation.chatList.components

//import androidx.compose.runtime.Composable
//import coil3.compose.rememberAsyncImagePainter
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fluent.R
import com.example.fluent.ui.theme.Brown
import com.example.fluent.ui.theme.Peach
import com.example.fluent.ui.theme.PeachWhite

@Preview(showBackground = true)
@Composable
fun ChatListItem() {
    Surface(
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .clickable(onClick = {}),
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
                        text = "User1",
                        style = MaterialTheme.typography.titleLarge,
                        color = Brown,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "09:09",
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
                    text = "this is a new message",
                    style = MaterialTheme.typography.labelLarge,
                    color = Brown.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}