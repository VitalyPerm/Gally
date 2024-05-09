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
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.MediaItem
import ru.kvf.feature.R
import ru.kvf.feature.mediabsh.MediaBSHUi

@Composable
fun TrashUi(component: TrashComponent) {
    val media by component.media.collectAsState()
    val gridCount by component.gridCount.collectAsState()
    DefaultContainer(
        titleRes = R.string.trash,
        onReverseClick = component::onReverseClick,
        onGridCountClick = component::onGridCountClick,
        gridCount = gridCount
    ) {
        LazyVerticalGrid(
            state = rememberLazyGridState(),
            columns = GridCells.Fixed(gridCount),
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

    MediaBSHUi(component.mediaBSHComponent)
}
