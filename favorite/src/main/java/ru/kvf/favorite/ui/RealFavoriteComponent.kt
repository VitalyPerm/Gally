package ru.kvf.favorite.ui

import com.arkivanov.decompose.ComponentContext
import ru.kvf.core.ComponentFactory
import ru.kvf.favorite.createFavoriteFoldersComponent
import ru.kvf.favorite.createFavoriteMediaComponent
import ru.kvf.favorite.ui.folders.FavoriteFoldersComponent
import ru.kvf.favorite.ui.media.FavoriteMediaComponent

class RealFavoriteComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory
) : ComponentContext by componentContext, FavoriteComponent {

    override val favoriteFoldersComponent: FavoriteFoldersComponent =
        componentFactory.createFavoriteFoldersComponent(componentContext)

    override val favoriteMediaComponent: FavoriteMediaComponent =
        componentFactory.createFavoriteMediaComponent(componentContext)
}
