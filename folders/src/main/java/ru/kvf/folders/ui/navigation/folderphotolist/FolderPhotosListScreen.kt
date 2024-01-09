package ru.kvf.folders.ui.navigation.folderphotolist

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.PhotosListWithDate

@Composable
fun FolderDetailsScreen(
    folderName: String,
    vm: FolderPhotosListViewModel = koinViewModel { parametersOf(folderName) },
    navigateToPhotoDetails: (photoId: Long, reverse: Boolean) -> Unit,
) {
    val state by vm.collectAsState()
    val gridState = rememberLazyGridState()

    vm.collectSideEffect {
        when (it) {
            FolderPhotosListSideEffect.ScrollUp -> gridState.animateScrollToItem(0)
        }
    }

    DefaultContainer(
        titleString = folderName,
        reverseActionEnable = true,
        onReverseClick = vm::onReverseClick
    ) {
        val photos = remember(state) {
            with(state) { if (reversed) reversedPhotos else photos }.toImmutableMap()
        }
        PhotosListWithDate(
            photos = photos,
            likedPhotos = state.likedPhotos.toImmutableList(),
            gridState = gridState,
            onPhotoClick = { navigateToPhotoDetails(it, state.reversed) },
            onLikedClick = vm::onLikeClick
        )
    }
}
