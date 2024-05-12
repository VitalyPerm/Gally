package ru.kvf.feature.favorite

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.kvf.core.ComponentFactory
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.createFavoriteFoldersComponent
import ru.kvf.createFavoriteMediaComponent
import ru.kvf.feature.favorite.folders.FavoriteFoldersComponent
import ru.kvf.feature.favorite.media.FavoriteMediaComponent

class RealFavoriteComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    private val onOutput: (FavoriteComponent.Output) -> Unit,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase
) : ComponentContext by componentContext, FavoriteComponent {

    private val componentScope = coroutineScope()

    override val favoriteFoldersComponent: FavoriteFoldersComponent =
        componentFactory.createFavoriteFoldersComponent(
            componentContext = componentContext,
            output = ::favoriteFoldersOutput
        )

    override val favoriteMediaComponent: FavoriteMediaComponent =
        componentFactory.createFavoriteMediaComponent(componentContext)

    override val gridCellsCount = gridCellsCountChangeUseCase
        .get(GridCellsCountChangeUseCase.Screen.Favorite)
        .stateIn(componentScope, SharingStarted.Lazily, 1)

    override val isReversed = MutableStateFlow(false)

    override fun onReverseClick() {
        isReversed.update { !it }
    }

    override fun onGridCountClick() {
        componentScope.safeLaunch {
            gridCellsCountChangeUseCase.set(
                value = gridCellsCount.value,
                screen = GridCellsCountChangeUseCase.Screen.Favorite
            )
        }
    }

    private fun favoriteFoldersOutput(output: FavoriteFoldersComponent.Output) {
        when (output) {
            is FavoriteFoldersComponent.Output.OpenFolderRequested ->
                onOutput(FavoriteComponent.Output.OpenFolderRequested(output.name))
        }
    }
}
