package ru.kvf.favorite.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface FavoriteListComponent {

    val state: StateFlow<FavoriteListState>
    val sideEffect: Flow<FavoriteListSideEffect>

    fun onLikeClick(id: Long)
    fun onPhotoClick(photoId: Long)
    fun onReverseClick()

    sealed interface Output {
        data class OpenPhotoRequested(val index: Int, val reversed: Boolean) : Output
    }
}
