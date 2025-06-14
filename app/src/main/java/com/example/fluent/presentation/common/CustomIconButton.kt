package com.example.fluent.presentation.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fluent.R
import com.example.fluent.ui.theme.MatteWhiteGrayMedium
import com.example.fluent.ui.theme.Peach
import com.example.fluent.ui.theme.PeachWhite

@Composable
fun CustomIconButton(
    onClick: ()->Unit,
    @DrawableRes icon: Int,
    contentDescription: String?,
    tint: Color = Color.Unspecified,
) {
    IconButton(
        onClick = { onClick() },
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
            )
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = tint
        )
    }
}