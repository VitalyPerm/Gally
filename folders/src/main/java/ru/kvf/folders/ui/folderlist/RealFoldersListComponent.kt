package ru.kvf.folders.ui.folderlist

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.componentCoroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.folders.domain.GetFoldersUseCase

class RealFoldersListComponent(
    componentContext: ComponentContext,
    private val onOutput: (FoldersListComponent.Output) -> Unit,
    getFoldersUseCase: GetFoldersUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
) : ComponentContext by componentContext, FoldersListComponent {

    private companion object {
        const val MAX_GRID_COUNT = 4
        const val MIN_GRID_COUNT = 1
    }

    private val scope = componentCoroutineScope()

    override val state = MutableStateFlow(FoldersListState())

    init {
        scope.collectFlow(getFoldersUseCase()) { folders ->
            state.update { state.value.copy(folders = folders) }
        }

        scope.collectFlow(
            gridCellsCountChangeUseCase.get(GridCellsCountChangeUseCase.Screen.FoldersList)
        ) { count ->
            state.update { state.value.copy(gridCellsCount = count) }
        }
    }

    override fun onGridCountClick() {
        val currentCount = state.value.gridCellsCount
        val value = if (currentCount == MAX_GRID_COUNT) MIN_GRID_COUNT else currentCount + 1
        scope.safeLaunch {
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
