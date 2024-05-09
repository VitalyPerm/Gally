package ru.kvf.core.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.kvf.core.R

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

@Composable
fun TrashBottomMenu(
    onDeleteClick: () -> Unit,
    onShareClick: () -> Unit,
    onUnTrashClick: () -> Unit,
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
            onClick = onUnTrashClick,
            imageVector = Icons.Default.RestoreFromTrash
        )
        MediaSelectModeMenuItem(
            onClick = onDeleteClick,
            imageVector = Icons.Default.DeleteForever
        )
    }
}

@Composable
fun BottomMenuCounter(
    onCloseClick: () -> Unit,
    selectedMediaCount: Int
) {
    var count by remember { mutableIntStateOf(0) }
    LaunchedEffect(selectedMediaCount) {
        if (selectedMediaCount > 0) count = selectedMediaCount
    }
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .size(36.dp)
            )
        }

        Text(
            text = stringResource(R.string.selected_items_count, count),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(end = 16.dp)
                .padding(vertical = 16.dp)
        )
    }
}
