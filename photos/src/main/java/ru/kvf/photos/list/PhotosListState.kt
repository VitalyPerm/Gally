package ru.kvf.photos.list

import androidx.compose.runtime.Stable
import ru.kvf.core.data.CustomDate
import ru.kvf.core.domain.Folder
import ru.kvf.core.domain.Photo

@Stable
data class PhotosListState(
    val photos: Map<CustomDate, List<Photo>> = emptyMap(),
    val likedPhotos: List<Long> = emptyList(),
    val folders: List<Folder> = emptyList(),
    val loading: Boolean = false,
    val noPhotosFound: Boolean = false,
    val showFolders: Boolean = false,
    val reversed: Boolean = false
)

sealed interface PhotosListSideEffect {
    data object ScrollUp : PhotosListSideEffect
}
