package ru.kvf.folders.ui.folderlist

import kotlinx.coroutines.flow.StateFlow

interface FoldersListComponent {

    val state: StateFlow<FoldersListState>

    fun onGridCountClick()

    fun onFolderClick(name: String)

    sealed interface Output {
        data class OpenFolderRequested(val name: String) : Output
    }
}
