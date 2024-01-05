package ru.kvf.favorite.ui.details

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.Photo

@Stable
data class PhotoDetailsState(
    val startIndex: Int = -1,
    val photos: List<Photo> = emptyList(),
    val loading: Boolean = true
)

sealed interface DetailsSideEffect
