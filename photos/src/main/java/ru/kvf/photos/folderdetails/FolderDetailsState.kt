package ru.kvf.photos.folderdetails

import ru.kvf.core.domain.entities.Photo

data class FolderDetailsState(
    val photos: List<Photo> = emptyList(),
    val loading: Boolean = true
)

sealed interface FolderDetailsSideEffect
