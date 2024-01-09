package ru.kvf.folders.ui.navigation.folderphotolist

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.PhotosListWithDate

@Composable
fun FolderDetailsScreen(
    folderName: String,
    vm: FolderPhotosListViewModel = koinViewModel {
        parametersOf(folderName)
    },
    navigateToPhotoDetails: (photoId: Long) -> Unit,
) {
    val state by vm.collectAsState()

    DefaultContainer(
        titleString = folderName,
        reverseActionEnable = true,
        onReverseClick = {}
    ) {
        PhotosListWithDate(
            photos = state.photos.toImmutableMap(),
            likedPhotos = state.likedPhotos.toImmutableList(),
            gridState = rememberLazyGridState(),
            onPhotoClick = navigateToPhotoDetails,
            onLikedClick = vm::onLikeClick
        )
    }
}
