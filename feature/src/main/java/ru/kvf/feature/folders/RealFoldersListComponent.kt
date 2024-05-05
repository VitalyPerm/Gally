package ru.kvf.feature.folders

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.kvf.core.domain.usecase.GetFoldersUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteFoldersIdsUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFolderFavoriteClickUseCase
import ru.kvf.core.utils.Constants
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch

class RealFoldersListComponent(
    componentContext: ComponentContext,
    private val onOutput: (FoldersListComponent.Output) -> Unit,
    getFoldersUseCase: GetFoldersUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
    private val handleFolderFavoriteClickUseCase: HandleFolderFavoriteClickUseCase,
    getFavoriteFoldersIdsUseCase: GetFavoriteFoldersIdsUseCase
) : ComponentContext by componentContext, FoldersListComponent {

    private val componentScope = coroutineScope()

    private val reversed = MutableStateFlow(false)
    override val folders = combine(getFoldersUseCase(), reversed) { all, reversed ->
        if (reversed) all.asReversed() else all
    }.stateIn(componentScope, SharingStarted.Lazily, emptyList())

    override val gridCellsCount = gridCellsCountChangeUseCase
        .get(GridCellsCountChangeUseCase.Screen.FoldersList)
        .stateIn(componentScope, SharingStarted.Lazily, 1)

    override val favoriteFolderIds: StateFlow<LongSet> = getFavoriteFoldersIdsUseCase()
        .stateIn(componentScope, SharingStarted.WhileSubscribed(5000), LongSet.EMPTY)

    override fun onGridCountClick() {
        val currentCount = gridCellsCount.value
        val value = if (currentCount == Constants.MAX_GRID_COUNT) Constants.MIN_GRID_COUNT else currentCount + 1
        componentScope.safeLaunch {
            gridCellsCountChangeUseCase.set(
                value = value,
                screen = GridCellsCountChangeUseCase.Screen.FoldersList
            )
        }
    }

    override fun onReverseClick() { reversed.update { !it } }

    override fun onFolderClick(name: String) {
        onOutput(FoldersListComponent.Output.OpenFolderRequested(name))
    }

    override fun onFolderLongClick(id: Long) {
        componentScope.safeLaunch {
            handleFolderFavoriteClickUseCase(id)
        }
    }
}
