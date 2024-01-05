package ru.kvf.photos.ui.list

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.PhotoDate
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.entities.Photo

@Stable
data class PhotosListState(
    val photos: Map<PhotoDate, List<Photo>> = emptyMap(),
    val likedPhotos: List<Long> = emptyList(),
    val folders: List<Folder> = emptyList(),
    val loading: Boolean = true,
    val noPhotosFound: Boolean = false,
    val showFolders: Boolean = false,
    val reversed: Boolean = false
)

sealed interface PhotosListSideEffect {
    data object ScrollUp : PhotosListSideEffect
}
