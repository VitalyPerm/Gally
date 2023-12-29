package ru.kvf.folders.ui.navigation.folderphotolist

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate

@Stable
data class FolderPhotosListState(
    val photos: Map<PhotoDate, List<Photo>> = emptyMap(),
    val reversedPhotos: Map<PhotoDate, List<Photo>> = emptyMap(),
    val likedPhotos: List<Long> = emptyList(),
    val reversed: Boolean = false
)

sealed interface FolderPhotosListSideEffect {
    data object ScrollUp : FolderPhotosListSideEffect
}
