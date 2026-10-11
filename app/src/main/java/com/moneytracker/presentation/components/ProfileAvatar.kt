package com.moneytracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.moneytracker.ui.theme.Indigo600
import com.moneytracker.ui.theme.White

@Composable
fun ProfileAvatar(
    name: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundColor: Color = Indigo600,
    textColor: Color = White,
    fontSize: Dp = (size.value * 0.42).dp,
    onClick: (() -> Unit)? = null
) {
    val initial = name.trim().take(1).uppercase().ifEmpty { "?" }
    val clickableModifier = if (onClick != null) modifier.clickable { onClick() } else modifier

    Box(
        modifier = clickableModifier
            .size(size)
            .clip(shape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank()) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .clip(shape),
                loading = {
                    Box(
                        modifier = Modifier
                            .size(size)
                            .background(backgroundColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initial,
                            fontSize = fontSize.value.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .size(size)
                            .background(backgroundColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initial,
                            fontSize = fontSize.value.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            )
        } else {
            Text(
                text = initial,
                fontSize = fontSize.value.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}
