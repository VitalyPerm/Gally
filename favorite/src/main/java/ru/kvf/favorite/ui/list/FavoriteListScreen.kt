package ru.kvf.favorite.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.widgets.PhotoItem
import ru.kvf.core.widgets.ReverseIcon
import ru.kvf.core.widgets.TopBar
import ru.kvf.favorite.R

@Composable
fun FavoriteListScreen(
    vm: FavoriteListViewModel = koinViewModel(),
    navigateToDetails: (Long) -> Unit
) {
    val state by vm.collectAsState()
    val photosListGridState = rememberLazyGridState()

    vm.collectSideEffect {
        when (it) {
            FavoriteListSideEffect.ScrollUp -> photosListGridState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBar(
            title = stringResource(R.string.likes),
            actions = {
                ReverseIcon(vm::onReverseIconClick)
            }
        )

        PhotosList(
            photos = state.photos,
            gridState = photosListGridState,
            onPhotoClick = navigateToDetails,
            onLikedClick = vm::onLikeClick
        )
    }
}

@Composable
private fun PhotosList(
    photos: List<Photo>,
    gridState: LazyGridState,
    onPhotoClick: (Long) -> Unit,
    onLikedClick: (Long) -> Unit
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(photos, key = { item: Photo -> item.id }) { photo ->
            PhotoItem(
                model = photo.uri,
                liked = true,
                shouldShowLikeIcon = false,
                onClick = { onPhotoClick(photo.id) },
                onLiked = { onLikedClick(photo.id) }
            )
        }
    }
}
