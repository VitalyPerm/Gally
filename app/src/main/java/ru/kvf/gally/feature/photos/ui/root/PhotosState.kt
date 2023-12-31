package ru.kvf.gally.feature.photos.ui.root

import ru.kvf.core.domain.Folder
import ru.kvf.core.domain.Photo

data class PhotosState(
    val photos: List<Photo> = emptyList(),
    val folders: List<Folder> = emptyList(),
    val loading: Boolean = false,
    val noPhotosFound: Boolean = false,
    val showFolders: Boolean = false
)

sealed interface PhotosSideEffect
