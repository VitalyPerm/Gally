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
import ru.kvf.core.widgets.MediaItem
import ru.kvf.feature.mediabsh.MediaBSHUi

@Composable
fun FavoriteMediaUi(
    component: FavoriteMediaComponent
) {
    val media by component.media.collectAsState()
    val isReversed by component.isReversed.collectAsState()
    val mediaList = if (isReversed) media.reversed() else media

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MediaList(
            media = mediaList,
            onMediaClick = component::onMediaClick,
            onMediaLongClick = {}
        )
    }

    MediaBSHUi(component.mediaBSHComponent)
}

@Composable
private fun MediaList(
    media: List<Media>,
    onMediaClick: (Long) -> Unit,
    onMediaLongClick: (Long) -> Unit,
) {
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(media, key = { item: Media -> item.id }) { media ->
            MediaItem(
                model = media.uri,
                favorite = true,
                shouldShowFavoriteIcon = false,
                onClick = { onMediaClick(media.id) },
                onLongClick = { onMediaLongClick(media.id) },
                cellsCount = 1
            )
        }
    }
}
