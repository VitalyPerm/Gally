package ru.kvf.core.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate

@Composable
fun PhotosListWithDate(
    photos: ImmutableMap<PhotoDate, List<Photo>>,
    likedPhotos: ImmutableList<Long>,
    gridState: LazyGridState,
    cellsCount: Int = 3,
    onPhotoClick: (Long) -> Unit,
    onLikedClick: (Long) -> Unit
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(cellsCount),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        photos.forEach { (date, photos) ->
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = date.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
            items(photos, key = { item: Photo -> item.id }) { photo ->
                PhotoItem(
                    model = photo.uri,
                    liked = photo.id in likedPhotos,
                    onClick = { onPhotoClick(photo.id) },
                    onLiked = { onLikedClick(photo.id) }
                )
            }
        }
    }
}
