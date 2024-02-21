package ru.kvf.photos.ui.detail

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.Photo

@Stable
data class PhotoState(
    val photos: List<Photo> = emptyList(),
    val currentIndex: Int? = null,
    val titleVisible: Boolean = false,
    val title: String = "",
    val reversed: Boolean = false
)
