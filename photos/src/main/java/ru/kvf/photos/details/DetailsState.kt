package ru.kvf.photos.details

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.Photo

@Stable
data class DetailsState(
    val startIndex: Int = 0,
    val photos: List<Photo> = emptyList(),
    val loading: Boolean = true
)

sealed interface DetailsSideEffect
