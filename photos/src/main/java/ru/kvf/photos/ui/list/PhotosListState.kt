package ru.kvf.photos.ui.list

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate

@Stable
data class PhotosListState(
    val photos: Map<PhotoDate, List<Photo>> = emptyMap(),
    val reversedPhotos: Map<PhotoDate, List<Photo>> = emptyMap(),
    val likedPhotos: List<Long> = emptyList(),
    val reversed: Boolean = false,
    val gridCellsCount: Int = 1
)

sealed interface PhotosListSideEffect {
    data object ScrollUp : PhotosListSideEffect
    data class NavigateToDetails(val index: Int) : PhotosListSideEffect
}
