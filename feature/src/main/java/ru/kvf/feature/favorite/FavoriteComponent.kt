package ru.kvf.feature.favorite

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.feature.favorite.folders.FavoriteFoldersComponent
import ru.kvf.feature.favorite.media.FavoriteMediaComponent

interface FavoriteComponent {

    val favoriteFoldersComponent: FavoriteFoldersComponent
    val favoriteMediaComponent: FavoriteMediaComponent
    val gridCellsCount: StateFlow<Int>
    val isReversed: StateFlow<Boolean>

    fun onGridCountClick()
    fun onReverseClick()

    sealed interface Output {
        data class OpenFolderRequested(val name: String) : Output
        data class MediaDetailsRequested(val mediaId: Long) : Output
    }
}
