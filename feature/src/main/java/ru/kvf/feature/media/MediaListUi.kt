package ru.kvf.feature.media

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.MediaDateSet
import ru.kvf.core.utils.MediaMap
import ru.kvf.core.utils.collectOnStart
import ru.kvf.core.widgets.BottomMenuCounter
import ru.kvf.core.widgets.MediaListWithDate
import ru.kvf.core.widgets.MediaSelectModeMenuItem
import ru.kvf.feature.mediabsh.MediaBSHUi

@Composable
fun MediaListUi(
    component: MediaListComponent,
    selectMediaModeEnable: MutableState<Boolean>? = null
) {
    val selectedMediaIds by component.selectedMediaIds.collectAsState()
    val media by component.mediaMap.collectAsState()
    val cellsCount by component.gridCellsCount.collectAsState()
    val sortReversed by component.sortReversed.collectAsState()
    val favoriteMediaIds by component.favoriteMediaIds.collectAsState()
    val selectedMediaDates by component.selectedMediaDates.collectAsState()

    val mediaListGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = component.lastPosition
    )

    BackHandler(enabled = selectedMediaIds.data.isNotEmpty()) {
        component.onSelectMediaDismiss()
    }

    DisposableEffect(Unit) {
        onDispose { component.savePosition(mediaListGridState.firstVisibleItemIndex) }
    }

    LaunchedEffect(selectedMediaIds) {
        selectMediaModeEnable?.value = selectedMediaIds.data.isNotEmpty()
    }

    component.scrollUp.collectOnStart {
        mediaListGridState.animateScrollToItem(0)
    }

    Content(
        media = media.first,
        reversedMedia = media.second,
        folderName = component.folderName,
        cellsCount = cellsCount,
        gridState = mediaListGridState,
        onGridCountClick = component::onGridCountClick,
        sortReversed = sortReversed,
        onReverseClick = component::onReverseClick,
        favoriteMediaIds = favoriteMediaIds,
        onMediaClick = component::onMediaClick,
        onMediaLongClick = component::onMediaLongClick,
        selectedMediaIds = selectedMediaIds,
        selectModeOnShareClick = component::selectModeOnShareClick,
        selectModeOnTrashClick = component::selectModeOnTrashClick,
        selectModeOnCloseClick = component::onSelectMediaDismiss,
        selectModeOnFavoriteClick = component::selectModeOnFavoriteClick,
        selectModeOnDisFavoriteClick = component::selectModeOnDisFavoriteClick,
        editMode = selectMediaModeEnable?.value ?: false,
        selectedMediaDates = selectedMediaDates,
        onSelectDateClick = component::onSelectDateClick,
    )

    MediaBSHUi(
        component = component.mediaBSHComponent,
        isReversed = sortReversed
    )
}

@Composable
private fun Content(
    media: MediaMap,
    reversedMedia: MediaMap,
    folderName: String?,
    cellsCount: Int,
    gridState: LazyGridState,
    onGridCountClick: () -> Unit,
    sortReversed: Boolean,
    onReverseClick: () -> Unit,
    favoriteMediaIds: LongSet,
    onMediaClick: (Long) -> Unit,
    onMediaLongClick: (Media) -> Unit,
    selectedMediaIds: LongSet,
    selectModeOnShareClick: () -> Unit,
    selectModeOnCloseClick: () -> Unit,
    selectModeOnTrashClick: () -> Unit,
    selectModeOnFavoriteClick: () -> Unit,
    selectModeOnDisFavoriteClick: () -> Unit,
    onSelectDateClick: (MediaDate) -> Unit,
    selectedMediaDates: MediaDateSet,
    editMode: Boolean,
) {
    Box {
        val mediaMap =
            remember(sortReversed, media) { if (sortReversed) reversedMedia else media }
        MediaListWithDate(
            media = mediaMap,
            favoriteMediaIds = favoriteMediaIds,
            gridState = gridState,
            cellsCount = cellsCount,
            onMediaClick = onMediaClick,
            onMediaLongClick = onMediaLongClick,
            selectedMediaIds = selectedMediaIds,
            selectedMediaDates = selectedMediaDates,
            onSelectDateClick = onSelectDateClick
        )

        MediaSelectModeMenu(
            visible = editMode,
            onShareClick = selectModeOnShareClick,
            onTrashClick = selectModeOnTrashClick,
            onFavoriteClick = selectModeOnFavoriteClick,
            onDisFavoriteClick = selectModeOnDisFavoriteClick,
            selectedMediaCount = selectedMediaIds.data.size,
            onCloseClick = selectModeOnCloseClick
        )
    }
}

@Composable
private fun BoxScope.MediaSelectModeMenu(
    visible: Boolean,
    onShareClick: () -> Unit,
    onTrashClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDisFavoriteClick: () -> Unit,
    selectedMediaCount: Int,
    onCloseClick: () -> Unit
) {
    var count by remember { mutableIntStateOf(0) }
    LaunchedEffect(selectedMediaCount) {
        if (selectedMediaCount > 0) count = selectedMediaCount
    }
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

                SelectModeMenuItems(
                    onShareClick = onShareClick,
                    onTrashClick = onTrashClick,
                    onFavoriteClick = onFavoriteClick,
                    onDisFavoriteClick = onDisFavoriteClick
                )
            }
        }
    }
}

@Composable
private fun SelectModeMenuItems(
    onShareClick: () -> Unit,
    onTrashClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDisFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
    ) {
        MediaSelectModeMenuItem(
            onClick = onShareClick,
            imageVector = Icons.Default.Share
        )

        MediaSelectModeMenuItem(
            onClick = onTrashClick,
            imageVector = Icons.Default.Delete
        )

        MediaSelectModeMenuItem(
            onClick = onFavoriteClick,
            imageVector = Icons.Default.Favorite
        )

        MediaSelectModeMenuItem(
            onClick = onDisFavoriteClick,
            imageVector = Icons.Default.HeartBroken
        )
    }
}
