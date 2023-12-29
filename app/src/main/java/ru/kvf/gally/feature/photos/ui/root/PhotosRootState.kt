package ru.kvf.gally.feature.photos.ui.root

import ru.kvf.gally.feature.photos.domain.Photo

data class PhotosRootState(
    val photos: List<Photo> = emptyList()
)

sealed interface PhotosRootSideEffect
