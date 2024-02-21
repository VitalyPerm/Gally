package ru.kvf.favorite.ui.list

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface FavoriteListComponent {

    val state: StateFlow<FavoriteListState>
    val sideEffect: SharedFlow<FavoriteListSideEffect>

    fun onLikeClick(id: Long)

    fun onReverseIconClick()
}
