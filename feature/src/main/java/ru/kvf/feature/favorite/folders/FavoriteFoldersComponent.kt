package ru.kvf.feature.favorite.folders

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.utils.FolderList

interface FavoriteFoldersComponent {

    val folders: StateFlow<FolderList>

    fun onFolderClick(name: String)
    fun onFolderLongClick(id: Long)

    sealed interface Output {
        data class OpenFolderRequested(val name: String) : Output
    }
}
