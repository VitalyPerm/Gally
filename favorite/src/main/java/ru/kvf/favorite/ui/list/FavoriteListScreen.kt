package ru.kvf.favorite.ui.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.PhotoItem
import ru.kvf.favorite.R

@Composable
fun FavoriteListScreen(
    vm: FavoriteListViewModel = koinViewModel(),
    navigateToDetails: (Long) -> Unit,
    isScrollInProgress: MutableState<Boolean>,
) {
    val state by vm.collectAsState()
    val favoriteListGridState = rememberLazyGridState()

    LaunchedEffect(favoriteListGridState.isScrollInProgress) {
        isScrollInProgress.value = favoriteListGridState.isScrollInProgress
    }

    vm.collectSideEffect {
        when (it) {
            FavoriteListSideEffect.ScrollUp -> favoriteListGridState.animateScrollToItem(0)
        }
    }

    DefaultContainer(
        titleRes = R.string.likes,
        reverseActionEnable = true,
        onReverseClick = vm::onReverseIconClick
    ) {
        PhotosList(
            photos = state.photos,
            gridState = favoriteListGridState,
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
        columns = GridCells.Fixed(2),
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
