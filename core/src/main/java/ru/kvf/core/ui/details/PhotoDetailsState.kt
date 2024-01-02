package ru.kvf.core.ui.details

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.Photo

@Stable
data class PhotoDetailsState(
    val startIndex: Int = 0,
    val photos: List<Photo> = emptyList(),
    val loading: Boolean = true
)

sealed interface DetailsSideEffect
