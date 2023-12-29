package ru.kvf.folders.ui.navigation.folderlist

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.ui.VM
import ru.kvf.folders.domain.GetFoldersUseCase

class FoldersListViewModel(
    getFoldersUseCase: GetFoldersUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
) : VM<FoldersListState, Nothing>(FoldersListState()) {

    init {
        collectFlow(getFoldersUseCase()) { updateFolders(it) }

        collectFlow(gridCellsCountChangeUseCase.get(GridCellsCountChangeUseCase.Screen.FoldersList)) {
            intent { reduce { state.copy(gridCellsCount = it) } }
        }
    }

    private fun updateFolders(folders: List<Folder>) = intent {
        reduce { state.copy(folders = folders) }
    }

    fun onGridCountClick() = intent {
        val value = if (state.gridCellsCount == 4) 1 else state.gridCellsCount + 1
        gridCellsCountChangeUseCase.set(
            value = value,
            screen = GridCellsCountChangeUseCase.Screen.FoldersList
        )
    }
}
