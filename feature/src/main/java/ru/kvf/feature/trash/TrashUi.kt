package ru.kvf.feature.trash

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.widgets.MediaItem

@Composable
fun TrashUi(component: TrashComponent) {
    val media by component.trash.collectAsState()
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(media.data, key = { item: Media -> item.id }) { media ->
            MediaItem(
                model = media.uri,
                onClick = { component.onMediaClick(media.id) },
                onLongClick = { component.onMediaLongClick(media.id) },
                cellsCount = 3,
                shouldShowFavoriteIcon = false
            )
        }
    }
}