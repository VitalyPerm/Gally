package ru.kvf.core.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.size.Size

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaItem(
    model: Any?,
    title: String? = null,
    favorite: Boolean = false,
    shouldShowFavoriteIcon: Boolean = true,
    duration: String? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    isSelected: Boolean = false,
    editMode: Boolean = false,
    cellsCount: Int
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .border(
                BorderStroke(4.dp, MaterialTheme.colorScheme.onPrimary),
                MaterialTheme.shapes.medium
            )
    ) {
        val scale by animateFloatAsState(targetValue = if (isSelected) 0.7f else 1f, label = "")
        val imageSize = remember(cellsCount) { calculatePhotoSize(cellsCount) }
        val favoriteIconSize = remember(cellsCount) { calculateFavoriteIconSize(cellsCount) }

        Column {
            ImageWithLoader(
                model = model,
                contentScale = ContentScale.Crop,
                size = imageSize,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale)
                    .clip(MaterialTheme.shapes.medium)
                    .combinedClickable(
                        onClick = { onClick?.invoke() },
                        onLongClick = { onLongClick?.invoke() }
                    )
            )
        }

        title?.let {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.7f)
                    .padding(3.dp)
                    .background(MaterialTheme.colorScheme.onPrimary, MaterialTheme.shapes.extraLarge)
                    .padding(6.dp)
            )
        }

        if (favorite && shouldShowFavoriteIcon) {
            Icon(
                tint = Color.Red.copy(alpha = 0.5f),
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .size(favoriteIconSize)
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
                imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            )
        }
    }
}

private fun calculateFavoriteIconSize(cellsCount: Int): Dp = when (cellsCount) {
    1 -> 48.dp
    2 -> 36.dp
    3 -> 24.dp
    else -> 16.dp
}

private fun calculatePhotoSize(cellsCount: Int): Size = when (cellsCount) {
    1 -> Size(1000, 1000)
    2 -> Size(750, 750)
    3 -> Size(500, 500)
    else -> Size(250, 250)
}
