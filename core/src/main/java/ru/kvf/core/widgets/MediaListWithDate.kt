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
    onLikedClick: (Long) -> Unit
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
            items(media, key = { item: Media -> item.id }) { media ->
                MediaItem(
                    model = media.uri,
                    liked = media.id in likedMedia,
                    duration = media.duration,
                    onClick = { onMediaClick(media.id) },
                    onLiked = { onLikedClick(media.id) },
                    onLongClick = { onMediaLongClick(media) }
                )
            }
        }
    }
}
