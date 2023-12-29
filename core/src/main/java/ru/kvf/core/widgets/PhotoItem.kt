package ru.kvf.core.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.HeartBroken
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.size.Size
import kotlinx.coroutines.delay
import ru.kvf.core.utils.Constants

@Composable
fun PhotoItem(
    model: Any,
    liked: Boolean,
    shouldShowLikeIcon: Boolean = true,
    onClick: () -> Unit,
    onLiked: () -> Unit
) {
    var showLike by remember { mutableStateOf(false) }
    val hearSize by animateFloatAsState(targetValue = if (showLike) 100f else 0f, label = "")
    LaunchedEffect(key1 = showLike, block = {
        delay(Constants.PHOTO_ITEM_LIKE_DURATION)
        showLike = false
    })

    Box(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        ImageWithLoader(
            model = model,
            contentScale = ContentScale.Crop,
            size = Size(250, 250),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(1.dp)
                .clip(MaterialTheme.shapes.medium)
                .border(BorderStroke(3.dp, MaterialTheme.colorScheme.onPrimary),MaterialTheme.shapes.medium)
                .clickable(onClick = onClick)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            showLike = true
                            onLiked()
                        },
                        onTap = {
                            onClick()
                        }
                    )
                }
        )

        Icon(
            tint = Color.Red,
            imageVector = if (liked) Icons.Rounded.HeartBroken else Icons.Filled.Favorite,
            contentDescription = null,
            modifier = Modifier
                .size(hearSize.dp)
                .align(Alignment.Center)
        )

        if (liked && shouldShowLikeIcon) {
            Icon(
                tint = Color.Red.copy(alpha = 0.3f),
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(15.dp)
                    .align(Alignment.TopEnd)
            )
        }
    }
}
