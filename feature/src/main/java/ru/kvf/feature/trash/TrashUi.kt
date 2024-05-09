package ru.kvf.feature.trash

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.widgets.BottomMenuCounter
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.MediaItem
import ru.kvf.core.widgets.TrashBottomMenu
import ru.kvf.feature.R
import ru.kvf.feature.mediabsh.MediaBSHUi

@Composable
fun TrashUi(component: TrashComponent) {
    val media by component.media.collectAsState()
    val gridCount by component.gridCount.collectAsState()
    val selectedMediaIds by component.selectedMediaIds.collectAsState()

    BackHandler(enabled = selectedMediaIds.data.isNotEmpty()) {
        component.onSelectMediaDismiss()
    }
    Box {
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
                        shouldShowFavoriteIcon = false,
                        isSelected = media.id in selectedMediaIds.data,
                    )
                }
            }
        }
        MediaSelectModeMenu(
            visible = selectedMediaIds.data.isNotEmpty(),
            onShareClick = component::selectModeOnShareClick,
            onTrashClick = component::selectModeOnUnTrashClick,
            onCloseClick = component::onSelectMediaDismiss,
            onDeleteClick = component::selectModeOnDeleteClick,
            selectedMediaCount = selectedMediaIds.data.size
        )
    }

    MediaBSHUi(component.mediaBSHComponent)
}

@Composable
private fun BoxScope.MediaSelectModeMenu(
    visible: Boolean,
    onShareClick: () -> Unit,
    onTrashClick: () -> Unit,
    onDeleteClick: () -> Unit,
    selectedMediaCount: Int,
    onCloseClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
    ) {
        AnimatedVisibility(visible) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 48.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                BottomMenuCounter(onCloseClick, selectedMediaCount)

                Spacer(modifier = Modifier.height(16.dp))

                TrashBottomMenu(
                    onShareClick = onShareClick,
                    onDeleteClick = onDeleteClick,
                    onUnTrashClick = onTrashClick
                )
            }
        }
    }
}
