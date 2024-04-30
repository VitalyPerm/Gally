@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kvf.favorite.ui.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.widgets.MediaBSH
import ru.kvf.core.widgets.MediaItem

@Composable
fun FavoriteMediaUi(
    component: FavoriteMediaComponent
) {
    val media by component.media.collectAsState()
    val isReversed by component.isReversed.collectAsState()
    val favoriteListGridState = rememberLazyGridState()
    val mediaList = if (isReversed) media.reversed() else media

    Box(
        modifier = Modifier
        .padding(bottom = 4.dp)
    ) {
        MediaList(
            media = mediaList,
            gridState = favoriteListGridState,
            onMediaClick = component::onMediaClick,
            onLikedClick = component::onLikeClick,
            onMediaLongClick = {}
        )
    }

    MediaBSH(component.mediaBSHComponent)
}

@Composable
private fun MediaList(
    media: List<Media>,
    gridState: LazyGridState,
    onMediaClick: (Long) -> Unit,
    onMediaLongClick: (Long) -> Unit,
    onLikedClick: (Long) -> Unit
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(media, key = { item: Media -> item.id }) { media ->
            MediaItem(
                model = media.uri,
                liked = true,
                shouldShowLikeIcon = false,
                onClick = { onMediaClick(media.id) },
                onLongClick = { onMediaLongClick(media.id) },
                onLiked = { onLikedClick(media.id) },
            )
        }
    }
}
