package ru.kvf.gally.feature.photos.ui.root

import ru.kvf.gally.feature.photos.domain.Folder
import ru.kvf.gally.feature.photos.domain.Photo

data class PhotosState(
    val photos: List<Photo> = emptyList(),
    val folders: List<Folder> = emptyList(),
    val loading: Boolean = false,
    val noPhotosFound: Boolean = false,
    val showFolders: Boolean = false
)

sealed interface PhotosSideEffect
