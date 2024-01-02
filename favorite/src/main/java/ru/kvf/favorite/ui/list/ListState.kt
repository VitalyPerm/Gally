package ru.kvf.favorite.ui.list

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.Photo

@Stable
data class PhotosState(
    val photos: List<Photo> = emptyList(),
    val noPhotosFound: Boolean = false
)

sealed interface PhotosSideEffect {
    data object ScrollUp : PhotosSideEffect
}
