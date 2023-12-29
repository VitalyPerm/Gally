
package ru.kvf.photos.ui.list

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.PhotosListWithDate
import ru.kvf.photos.R

@Composable
fun PhotosListScreen(
    vm: PhotosListViewModel = koinViewModel(),
    navigateToPhotoDetails: (Int, Boolean) -> Unit,
    isScrollInProgress: MutableState<Boolean>,
) {
    val state by vm.collectAsState()
    val photosListGridState = rememberLazyGridState()

    LaunchedEffect(photosListGridState.isScrollInProgress) {
        isScrollInProgress.value = photosListGridState.isScrollInProgress
    }

    vm.collectSideEffect { effect ->
        when (effect) {
            PhotosListSideEffect.ScrollUp -> photosListGridState.animateScrollToItem(0)
            is PhotosListSideEffect.NavigateToDetails -> navigateToPhotoDetails(effect.index, state.reversed)
        }
    }

    DefaultContainer(
        titleRes = R.string.photos,
        gridCountActionEnable = true,
        gridCount = state.gridCellsCount,
        onGridCountClick = vm::onGridCountClick,
        reverseActionEnable = true,
        onReverseClick = vm::onReverseClick
    ) {
        val photos = remember(state) {
            with(state) { if (reversed) reversedPhotos else photos }.toImmutableMap()
        }
        PhotosListWithDate(
            photos = photos,
            likedPhotos = state.likedPhotos.toImmutableList(),
            gridState = photosListGridState,
            cellsCount = state.gridCellsCount,
            onPhotoClick = vm::calculatePhotoIndex,
            onLikedClick = vm::onLikeClick
        )
    }
}
