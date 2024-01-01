package ru.kvf.photos.list

import ru.kvf.core.data.CustomDate
import ru.kvf.core.domain.Folder
import ru.kvf.core.domain.Photo

data class PhotosState(
    val photos: Map<CustomDate, List<Photo>> = emptyMap(),
    val folders: List<Folder> = emptyList(),
    val loading: Boolean = false,
    val noPhotosFound: Boolean = false,
    val showFolders: Boolean = false,
    val reversed: Boolean = false
)

sealed interface PhotosSideEffect
