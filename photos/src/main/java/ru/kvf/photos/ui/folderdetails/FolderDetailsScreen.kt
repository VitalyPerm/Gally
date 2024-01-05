package ru.kvf.photos.ui.folderdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import ru.kvf.core.widgets.PhotosListWithDate

@Composable
fun FolderDetailsScreen(
    folderName: String,
    vm: FolderDetailsViewModel = koinViewModel {
        parametersOf(folderName)
    }
) {
    val state by vm.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        PhotosListWithDate(
            photos = state.photos.toImmutableMap(),
            likedPhotos = state.likedPhotos.toImmutableList(),
            gridState = rememberLazyGridState(),
            onPhotoClick = {},
            onLikedClick = vm::onLikeClick
        )
    }
}
