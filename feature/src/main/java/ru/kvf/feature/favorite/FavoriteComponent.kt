package ru.kvf.feature.favorite

import ru.kvf.feature.favorite.folders.FavoriteFoldersComponent
import ru.kvf.feature.favorite.media.FavoriteMediaComponent

interface FavoriteComponent {

    val favoriteFoldersComponent: FavoriteFoldersComponent

    val favoriteMediaComponent: FavoriteMediaComponent
}
