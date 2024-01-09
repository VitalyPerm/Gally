package ru.kvf.folders.ui.navigation.photodetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import ru.kvf.core.widgets.PhotosPager

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderPhotoDetailsScreen(
    folderName: String,
    photoId: Long,
    reverse: Boolean,
    viewModel: FolderPhotoDetailsViewModel = koinViewModel {
        parametersOf(photoId, folderName)
    }
) {
    val state by viewModel.collectAsState()

    if (state.loading.not()) {
        val pagerState = rememberPagerState(initialPage = state.startIndex) {
            state.photos.size
        }
        PhotosPager(
            photos = state.photos.toImmutableList(),
            pagerState = pagerState,
            reversePager = reverse
        )
    }
}
