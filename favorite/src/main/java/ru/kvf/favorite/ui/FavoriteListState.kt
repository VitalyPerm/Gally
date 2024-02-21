package ru.kvf.favorite.ui

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.Photo

@Stable
data class FavoriteListState(
    val photos: List<Photo> = emptyList(),
    val noPhotosFound: Boolean = false,
    val reversed: Boolean = false
)

sealed interface FavoriteListSideEffect {
    data object ScrollUp : FavoriteListSideEffect
}
