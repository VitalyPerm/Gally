package ru.kvf.photos.ui.list

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import ru.kvf.core.utils.Log
import ru.kvf.core.utils.collectSideEffect
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.PhotosListWithDate
import ru.kvf.photos.R

@Composable
fun PhotosListUi(
    component: PhotosListComponent,
    isScrollInProgress: MutableState<Boolean>? = null
) {
    val state by component.state.collectAsState()
    val photosListGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = state.lastPosition
    )

    LaunchedEffect(photosListGridState.isScrollInProgress) {
        isScrollInProgress?.value = photosListGridState.isScrollInProgress
    }

    DisposableEffect(Unit) {
        onDispose { component.savePosition(photosListGridState.firstVisibleItemIndex) }
    }

    component.sideEffect.collectSideEffect {
        when (it) {
            PhotosListSideEffect.ScrollUp -> {
                photosListGridState.animateScrollToItem(0)
            }
        }
    }

    DefaultContainer(
        titleRes = R.string.photos,
        titleString = state.folderName,
        gridCountActionEnable = true,
        gridCount = state.gridCellsCount,
        onGridCountClick = component::onGridCountClick,
        reverseActionEnable = true,
        onReverseClick = component::onReverseClick
    ) {
        val photos = remember(state) {
            with(state) { if (reversed) reversedPhotos else photos }.toImmutableMap()
        }
        PhotosListWithDate(
            photos = photos,
            likedPhotos = state.likedPhotos.toImmutableList(),
            gridState = photosListGridState,
            cellsCount = state.gridCellsCount,
            onPhotoClick = component::onPhotoClick,
            onLikedClick = component::onLikeClick
        )
    }
}
