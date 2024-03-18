package ru.kvf.feature.favorite.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import ru.kvf.core.utils.MediaList
import ru.kvf.core.widgets.MediaItem

@Composable
fun FavoriteMediaUi(
    component: FavoriteMediaComponent,
    isReversed: Boolean,
    gridCellsCount: Int
) {
    val media by component.media.collectAsState()
    val mediaList = if (isReversed) media.reversed() else media

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MediaList(
            media = mediaList,
            onMediaClick = component::onMediaClick,
            onMediaLongClick = component::onMediaLongClick,
            gridCellsCount = gridCellsCount
        )
    }
}

@Composable
private fun MediaList(
    media: MediaList,
    onMediaClick: (Long) -> Unit,
    onMediaLongClick: (Long) -> Unit,
    gridCellsCount: Int
) {
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Fixed(gridCellsCount),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(media.data, key = { item: Media -> item.id }) { media ->
            MediaItem(
                model = media.uri,
                onClick = { onMediaClick(media.id) },
                onLongClick = { onMediaLongClick(media.id) },
                cellsCount = gridCellsCount,
                shouldShowFavoriteIcon = false
            )
        }
    }
}
