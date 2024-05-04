package ru.kvf.core.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SelectModeMenuItems(
    onShareClick: () -> Unit,
    onTrashClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
    ) {
        MediaSelectModeMenuItem(
            onClick = onShareClick,
            imageVector = Icons.Default.Share
        )

        MediaSelectModeMenuItem(
            onClick = onTrashClick,
            imageVector = Icons.Default.Delete
        )

        MediaSelectModeMenuItem(
            onClick = onFavoriteClick,
            imageVector = Icons.Default.Favorite
        )
    }
}

@Composable
fun MediaSelectModeMenuItem(
    imageVector: ImageVector,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier
                .size(42.dp)
        )
    }
}
