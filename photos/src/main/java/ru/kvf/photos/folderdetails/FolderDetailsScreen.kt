package ru.kvf.photos.folderdetails

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
fun FolderDetailsScreen(
    folderName: String,
    viewModel: FolderDetailsViewModel = koinViewModel {
        parametersOf(folderName)
    }
) {
    val state by viewModel.collectAsState()

    val pagerState = rememberPagerState {
        state.photos.size
    }

    PhotosPager(photos = state.photos.toImmutableList(), pagerState = pagerState, loading = state.loading)
}
