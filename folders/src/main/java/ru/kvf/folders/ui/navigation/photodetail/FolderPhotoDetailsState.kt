package ru.kvf.folders.ui.navigation.photodetail

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.Photo

@Stable
data class FolderPhotoDetailsState(
    val startIndex: Int = 0,
    val photos: List<Photo> = emptyList(),
    val loading: Boolean = true
)
