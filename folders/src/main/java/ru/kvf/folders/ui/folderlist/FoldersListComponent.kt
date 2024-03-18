package ru.kvf.folders.ui.folderlist

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Folder

interface FoldersListComponent {

    val folders: StateFlow<List<Folder>>
    val gridCellsCount: StateFlow<Int>

    fun onGridCountClick()

    fun onFolderClick(name: String)

    sealed interface Output {
        data class OpenFolderRequested(val name: String) : Output
    }
}
