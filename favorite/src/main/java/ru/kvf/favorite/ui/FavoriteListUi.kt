package ru.kvf.favorite.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.utils.collectSideEffect
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.MediaItem
import ru.kvf.favorite.R

@Composable
fun FavoriteListUi(
    component: FavoriteListComponent,
    navBarPadding: Dp
) {
    val media by component.media.collectAsState()
    val isReversed by component.isReversed.collectAsState()

    val favoriteListGridState = rememberLazyGridState()

    component.sideEffect.collectSideEffect {
        when (it) {
            FavoriteListSideEffect.ScrollUp -> {
                favoriteListGridState.animateScrollToItem(0)
            }
        }
    }

    val mediaList = if (isReversed) media.reversed() else media

    DefaultContainer(
        titleRes = R.string.likes,
        reverseActionEnable = true,
        onReverseClick = component::onReverseClick,
        modifier = Modifier
            .padding(bottom = navBarPadding)
    ) {
        MediaList(
            media = mediaList,
            gridState = favoriteListGridState,
            onMediaClick = component::onMediaClick,
            onLikedClick = component::onLikeClick,
            onMediaLongClick = {}
        )
    }
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
