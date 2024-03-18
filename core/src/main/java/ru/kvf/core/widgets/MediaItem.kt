package ru.kvf.core.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.rounded.HeartBroken
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.size.Size
import kotlinx.coroutines.delay
import ru.kvf.core.utils.Constants
import ru.kvf.core.utils.Log

@Composable
fun MediaItem(
    model: Any,
    liked: Boolean = false,
    shouldShowLikeIcon: Boolean = true,
    duration: String? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onLiked: (() -> Unit)? = null,
    size: Size = Size(250, 250),
    isSelected: Boolean = false,
    editMode: Boolean = false
) {
    var showLike by remember { mutableStateOf(false) }
    val hearSize by animateFloatAsState(targetValue = if (showLike) 100f else 0f, label = "")
    LaunchedEffect(showLike) {
        delay(Constants.MEDIA_ITEM_LIKE_DURATION)
        showLike = false
    }
    var localIsSelected by remember { mutableStateOf(false) }
    LaunchedEffect(isSelected) {
        localIsSelected = isSelected
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .border(
                BorderStroke(4.dp, MaterialTheme.colorScheme.onPrimary),
                MaterialTheme.shapes.medium
            )
    ) {
        val scale by animateFloatAsState(targetValue = if (localIsSelected) 0.7f else 1f, label = "")

        ImageWithLoader(
            model = model,
            contentScale = ContentScale.Crop,
            size = size,
            modifier = Modifier
                .fillMaxSize()
                .scale(scale)
                .clip(MaterialTheme.shapes.medium)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            showLike = true
                            onLiked?.invoke()
                        },
                        onTap = {
                            localIsSelected = localIsSelected.not()
                            onClick?.invoke()
                        },
                        onLongPress = { onLongClick?.invoke() }
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
                tint = Color.Red.copy(alpha = 0.5f),
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .size(15.dp)
                    .align(Alignment.TopEnd)
            )
        }

        duration?.let {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        .padding(2.dp)
                )
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "play",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        AnimatedVisibility(editMode) {
            Icon(
                imageVector = if (localIsSelected) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            )
        }
    }
}
