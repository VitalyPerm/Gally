package ru.kvf.feature.favorite

import com.arkivanov.decompose.ComponentContext
import ru.kvf.core.ComponentFactory
import ru.kvf.createFavoriteFoldersComponent
import ru.kvf.createFavoriteMediaComponent
import ru.kvf.feature.favorite.folders.FavoriteFoldersComponent
import ru.kvf.feature.favorite.media.FavoriteMediaComponent

class RealFavoriteComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    private val onOutput: (FavoriteComponent.Output) -> Unit
) : ComponentContext by componentContext, FavoriteComponent {

    override val favoriteFoldersComponent: FavoriteFoldersComponent =
        componentFactory.createFavoriteFoldersComponent(
            componentContext = componentContext,
            output = ::favoriteFoldersOutput
        )

    override val favoriteMediaComponent: FavoriteMediaComponent =
        componentFactory.createFavoriteMediaComponent(componentContext)

    private fun favoriteFoldersOutput(output: FavoriteFoldersComponent.Output) {
        when (output) {
            is FavoriteFoldersComponent.Output.OpenFolderRequested ->
                onOutput(FavoriteComponent.Output.OpenFolderRequested(output.name))
        }
    }
}
