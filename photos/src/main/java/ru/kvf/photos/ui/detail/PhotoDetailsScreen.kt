package ru.kvf.photos.ui.detail

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
fun PhotosDetailsScreen(
    photoId: Long,
    reversePager: Boolean = false,
    viewModel: PhotoDetailsViewModel = koinViewModel {
        parametersOf(photoId)
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
            reversePager = reversePager
        )
    }
}
