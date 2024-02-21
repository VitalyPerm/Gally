package ru.kvf.favorite.ui.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.utils.collectSideEffect
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.PhotoItem
import ru.kvf.favorite.R

@Composable
fun FavoriteListUi(
    component: FavoriteListComponent,
    // navigateToDetails: (Long) -> Unit,
) {
    val state by component.state.collectAsState()
    val favoriteListGridState = rememberLazyGridState()

    component.sideEffect.collectSideEffect {
        when (it) {
            FavoriteListSideEffect.ScrollUp -> favoriteListGridState.animateScrollToItem(0)
        }
    }

    DefaultContainer(
        titleRes = R.string.likes,
        reverseActionEnable = true,
        onReverseClick = component::onReverseIconClick
    ) {
        PhotosList(
            photos = state.photos,
            gridState = favoriteListGridState,
            onPhotoClick = {},
            onLikedClick = component::onLikeClick
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
