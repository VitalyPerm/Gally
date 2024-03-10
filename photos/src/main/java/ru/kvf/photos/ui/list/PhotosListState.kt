package ru.kvf.photos.ui.list

import android.net.Uri
import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate

@Stable
data class PhotosListState(
    val photos: Map<PhotoDate, List<Photo>> = emptyMap(),
    val reversedPhotos: Map<PhotoDate, List<Photo>> = emptyMap(),
    val likedPhotos: List<Long> = emptyList(),
    val reversed: Boolean = false,
    val gridCellsCount: Int = 1,
    val folderName: String? = null,
    val lastPosition: Int = 0,
    val deletePhotosCandidate: Photo? = null
)

sealed interface PhotosListSideEffect {
    data object ScrollUp : PhotosListSideEffect
    data class DeletePhoto(val uri: Uri) : PhotosListSideEffect
}
