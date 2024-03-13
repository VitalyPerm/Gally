package ru.kvf.favorite.ui

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.Media

@Stable
data class FavoriteListState(
    val media: List<Media> = emptyList(),
    val noMediaFound: Boolean = false,
    val reversed: Boolean = false
)

sealed interface FavoriteListSideEffect {
    data object ScrollUp : FavoriteListSideEffect
}
