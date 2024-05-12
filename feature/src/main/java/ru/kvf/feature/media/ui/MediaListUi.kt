package ru.kvf.feature.media.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.MediaDateSet
import ru.kvf.core.utils.MediaMap
import ru.kvf.core.utils.collectOnStart
import ru.kvf.core.widgets.MediaListWithDate
import ru.kvf.core.widgets.MediaSelectModeMenu
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
        cellsCount = cellsCount,
        gridState = mediaListGridState,
        sortReversed = sortReversed,
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

    MediaBSHUi(component.mediaBSHComponent)
}

@Composable
private fun Content(
    media: MediaMap,
    reversedMedia: MediaMap,
    cellsCount: Int,
    gridState: LazyGridState,
    sortReversed: Boolean,
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
