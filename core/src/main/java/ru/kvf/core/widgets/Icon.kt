package ru.kvf.core.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WifiProtectedSetup
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ReverseIcon(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.WifiProtectedSetup,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun TrashIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.RestoreFromTrash,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun GridCountIcon(
    onClick: () -> Unit,
    count: Int
) {
    TextButton(onClick = onClick) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun PhotoIcon(
    onClick: () -> Unit,
    enable: Boolean,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Photo,
            contentDescription = null,
            tint = if (enable) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun VideoIcon(
    onClick: () -> Unit,
    enable: Boolean,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Videocam,
            contentDescription = null,
            tint = if (enable) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
        )
    }
}
