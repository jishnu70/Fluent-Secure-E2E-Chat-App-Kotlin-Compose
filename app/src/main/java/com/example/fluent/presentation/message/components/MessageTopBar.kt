package com.example.fluent.presentation.message.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fluent.R
import com.example.fluent.ui.theme.Brown
import com.example.fluent.ui.theme.MatteWhiteGrayMedium
import com.example.fluent.ui.theme.Peach
import com.example.fluent.ui.theme.PeachWhite

@Composable
fun MessageTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = PeachWhite,
        shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp),
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        MatteWhiteGrayMedium,
                        RoundedCornerShape(45)
                    )
                    .shadow(
                        elevation = 10.dp,
                        RoundedCornerShape(45),
                        ambientColor = Peach,
                        spotColor = PeachWhite
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = { onBackClick() },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back_button),
                        contentDescription = "Back",
                        tint = Color.Unspecified
                    )
                }
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            MatteWhiteGrayMedium,
                            RoundedCornerShape(45)
                        )
                        .shadow(
                            elevation = 10.dp,
                            RoundedCornerShape(45),
                            ambientColor = Peach,
                            spotColor = PeachWhite
                        )
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.user),
                        contentDescription = "User",
                        tint = Color.Unspecified,
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "User_1",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.W900,
                    color = Brown,
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(30.dp)
                    .padding(4.dp)
                    .background(
                        MatteWhiteGrayMedium,
                        RoundedCornerShape(45)
                    )
                    .shadow(
                        elevation = 10.dp,
                        RoundedCornerShape(45),
                        ambientColor = Peach,
                        spotColor = PeachWhite
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.dots),
                    contentDescription = "Dots",
                    tint = Brown
                )
            }
        }
    }
}