
package ru.kvf.photos.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.kvf.core.widgets.PhotosListWithDate
import ru.kvf.core.widgets.ReverseIcon
import ru.kvf.core.widgets.TopBar
import ru.kvf.photos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosListScreen(
    vm: PhotosListViewModel = koinViewModel(),
    isScrollInProgress: MutableState<Boolean>,
    navigateToPhotoDetails: (Long) -> Unit,
) {
    val state by vm.collectAsState()
    val photosListGridState = rememberLazyGridState()

    LaunchedEffect(photosListGridState.isScrollInProgress) {
        isScrollInProgress.value = photosListGridState.isScrollInProgress
    }

    vm.collectSideEffect {
        when (it) {
            PhotosListSideEffect.ScrollUp -> photosListGridState.animateScrollToItem(0)
        }
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                title = stringResource(R.string.photos),
                actions = {
                    ReverseIcon(vm::onReverseIconClick)
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        val photos = remember(state) {
            with(state) { if (reversed) reversedPhotos else normalPhotos }.toImmutableMap()
        }
        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            PhotosListWithDate(
                photos = photos,
                likedPhotos = state.likedPhotos.toImmutableList(),
                gridState = photosListGridState,
                onPhotoClick = navigateToPhotoDetails,
                onLikedClick = vm::onLikeClick
            )
        }
    }
}
