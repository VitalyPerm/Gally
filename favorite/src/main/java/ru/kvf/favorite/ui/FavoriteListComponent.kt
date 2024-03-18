package ru.kvf.favorite.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media

interface FavoriteListComponent {

    val media: StateFlow<List<Media>>
    val isReversed: StateFlow<Boolean>
    val sideEffect: Flow<FavoriteListSideEffect>

    fun onLikeClick(id: Long)
    fun onMediaClick(mediaId: Long)
    fun onReverseClick()

    sealed interface Output {
        data class OpenMediaRequested(val index: Int, val reversed: Boolean) : Output
    }
}
