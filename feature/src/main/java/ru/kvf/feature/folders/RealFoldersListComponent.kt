package ru.kvf.feature.folders

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.kvf.core.domain.usecase.GetFoldersUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteFoldersIdsUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFolderFavoriteClickUseCase
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch

class RealFoldersListComponent(
    componentContext: ComponentContext,
    private val onOutput: (FoldersListComponent.Output) -> Unit,
    getFoldersUseCase: GetFoldersUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
    private val handleFolderFavoriteClickUseCase: HandleFolderFavoriteClickUseCase,
    private val getFavoriteFoldersIdsUseCase: GetFavoriteFoldersIdsUseCase
) : ComponentContext by componentContext, FoldersListComponent {

    private val componentScope = coroutineScope()

    private companion object {
        const val MAX_GRID_COUNT = 4
        const val MIN_GRID_COUNT = 1
    }

    override val folders = getFoldersUseCase().stateIn(componentScope, SharingStarted.Lazily, emptyList())
    override val gridCellsCount = gridCellsCountChangeUseCase
        .get(GridCellsCountChangeUseCase.Screen.FoldersList)
        .stateIn(componentScope, SharingStarted.Lazily, 1)
    override val favoriteFolderIds: StateFlow<LongSet> = getFavoriteFoldersIdsUseCase()
        .stateIn(componentScope, SharingStarted.WhileSubscribed(5000), LongSet.EMPTY)

    override fun onGridCountClick() {
        val currentCount = gridCellsCount.value
        val value = if (currentCount == MAX_GRID_COUNT) MIN_GRID_COUNT else currentCount + 1
        componentScope.safeLaunch {
            gridCellsCountChangeUseCase.set(
                value = value,
                screen = GridCellsCountChangeUseCase.Screen.FoldersList
            )
        }
    }

    override fun onFolderClick(name: String) {
        onOutput(FoldersListComponent.Output.OpenFolderRequested(name))
    }
}
