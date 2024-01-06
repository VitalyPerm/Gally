package ru.kvf.photos.ui.list

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate

@Stable
data class PhotosListState(
    val normalPhotos: Map<PhotoDate, List<Photo>> = emptyMap(),
    val reversedPhotos: Map<PhotoDate, List<Photo>> = emptyMap(),
    val likedPhotos: List<Long> = emptyList(),
    val reversed: Boolean = true,
)

sealed interface PhotosListSideEffect {
    data object ScrollUp : PhotosListSideEffect
}
