package ru.kvf.core.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate

@Composable
fun MediaListWithDate(
    media: ImmutableMap<MediaDate, List<Media>>,
    likedMedia: ImmutableList<Long>,
    gridState: LazyGridState,
    cellsCount: Int = 3,
    onMediaClick: (Long) -> Unit,
    onMediaLongClick: (Media) -> Unit,
    onLikedClick: (Long) -> Unit,
    selectedMediaIds: ImmutableSet<Long>
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(cellsCount),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        media.forEach { (date, media) ->
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = date.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
            items(media, key = { item: Media -> item.id }) { item ->
                MediaItem(
                    model = item.uri,
                    liked = item.id in likedMedia,
                    duration = item.duration,
                    onClick = { onMediaClick(item.id) },
                    onLiked = { onLikedClick(item.id) },
                    onLongClick = { onMediaLongClick(item) },
                    selected = item.id in selectedMediaIds
                )
            }
        }
    }
}
