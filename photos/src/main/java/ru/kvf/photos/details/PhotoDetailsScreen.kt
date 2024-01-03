package ru.kvf.photos.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    viewModel: PhotoDetailsViewModel = koinViewModel {
        parametersOf(photoId)
    }
) {
    val state by viewModel.collectAsState()

    val pagerState = rememberPagerState {
        state.photos.size
    }
    LaunchedEffect(key1 = state.startIndex, block = {
        pagerState.scrollToPage(state.startIndex)
    })

    PhotosPager(photos = state.photos.toImmutableList(), pagerState = pagerState, loading = state.loading)
}
