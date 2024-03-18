package ru.kvf.core.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.size.Size
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate

@Composable
fun MediaListWithDate(
    media: Map<MediaDate, List<Media>>,
    likedMedia: List<Long>,
    gridState: LazyGridState,
    cellsCount: Int = 3,
    onMediaClick: (Long) -> Unit,
    onMediaLongClick: (Media) -> Unit,
    onLikedClick: (Long) -> Unit,
    selectedMediaIds: Set<Long>,
    selectedMediaDates: Set<MediaDate>,
    onSelectDateClick: (MediaDate) -> Unit
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(cellsCount),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        media.forEach { (date, media) ->
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = date.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    AnimatedVisibility(visible = selectedMediaIds.isNotEmpty()) {
                        val selected = remember(selectedMediaDates) { date in selectedMediaDates }
                        IconButton(onClick = { onSelectDateClick(date) }) {
                            Icon(
                                imageVector = if (selected) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
            items(media, key = { item: Media -> item.id }) { item ->
                MediaItem(
                    model = item.uri,
                    liked = item.id in likedMedia,
                    duration = item.duration,
                    onClick = { onMediaClick(item.id) },
                    onLiked = { onLikedClick(item.id) },
                    onLongClick = { onMediaLongClick(item) },
                    isSelected = item.id in selectedMediaIds,
                    size = calculatePhotoSize(cellsCount),
                    editMode = selectedMediaIds.isNotEmpty()
                )
            }
        }
    }
}

private fun calculatePhotoSize(cellsCount: Int): Size = when (cellsCount) {
    1 -> Size(1000, 1000)
    2 -> Size(750, 750)
    3 -> Size(500, 500)
    else -> Size(250, 250)
}
