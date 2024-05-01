package ru.kvf.feature.favorite

import com.arkivanov.decompose.ComponentContext
import ru.kvf.core.ComponentFactory
import ru.kvf.createFavoriteFoldersComponent
import ru.kvf.createFavoriteMediaComponent
import ru.kvf.feature.favorite.folders.FavoriteFoldersComponent
import ru.kvf.feature.favorite.media.FavoriteMediaComponent

class RealFavoriteComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory
) : ComponentContext by componentContext, FavoriteComponent {

    override val favoriteFoldersComponent: FavoriteFoldersComponent =
        componentFactory.createFavoriteFoldersComponent(componentContext)

    override val favoriteMediaComponent: FavoriteMediaComponent =
        componentFactory.createFavoriteMediaComponent(componentContext)
}
