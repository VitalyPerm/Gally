package ru.kvf.feature.folders.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.MediaDateSet
import ru.kvf.core.utils.MediaMap
import ru.kvf.core.utils.collectOnStart
import ru.kvf.core.widgets.GridCountIcon
import ru.kvf.core.widgets.MediaListWithDate
import ru.kvf.core.widgets.MediaSelectModeMenu
import ru.kvf.core.widgets.ReverseIcon
import ru.kvf.feature.mediabsh.MediaBSHUi

@Composable
fun FolderDetailsUi(component: FolderDetailsComponent) {
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
        editMode = selectedMediaIds.data.isNotEmpty(),
        selectedMediaDates = selectedMediaDates,
        onSelectDateClick = component::onSelectDateClick,
    )

    MediaBSHUi(
        component = component.mediaBSHComponent,
        isReversed = sortReversed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    media: MediaMap,
    reversedMedia: MediaMap,
    folderName: String,
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
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val mediaMap =
        remember(sortReversed, media) { if (sortReversed) reversedMedia else media }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
        ) {
            TopAppBar(
                title = { Text(text = folderName, style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary
                ),
                scrollBehavior = topBarScrollBehavior,
                actions = {
                    GridCountIcon(count = cellsCount, onClick = onGridCountClick)
                    ReverseIcon(onReverseClick)
                }
            )
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
        }

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
