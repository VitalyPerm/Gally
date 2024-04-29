package ru.kvf.favorite.ui

import ru.kvf.favorite.ui.folders.FavoriteFoldersComponent
import ru.kvf.favorite.ui.media.FavoriteMediaComponent

interface FavoriteComponent {

    val favoriteFoldersComponent: FavoriteFoldersComponent

    val favoriteMediaComponent: FavoriteMediaComponent
}
