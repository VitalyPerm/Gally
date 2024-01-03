package ru.kvf.photos.folderdetails

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate

@Stable
data class FolderDetailsState(
    val photos: Map<PhotoDate, List<Photo>> = emptyMap(),
    val likedPhotos: List<Long> = emptyList(),
    val loading: Boolean = true
)

sealed interface FolderDetailsSideEffect
