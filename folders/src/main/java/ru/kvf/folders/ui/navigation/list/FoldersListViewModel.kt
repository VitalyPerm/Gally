package ru.kvf.folders.ui.navigation.list

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.ui.VM
import ru.kvf.folders.domain.GetFoldersUseCase

class FoldersListViewModel(
    getFoldersUseCase: GetFoldersUseCase,
) : VM<FoldersListState, Nothing>(FoldersListState()) {

    init {
        collectFlow(getFoldersUseCase()) { updateFolders(it) }
    }

    private fun updateFolders(folders: List<Folder>) = intent {
        reduce { state.copy(folders = folders) }
    }
}
