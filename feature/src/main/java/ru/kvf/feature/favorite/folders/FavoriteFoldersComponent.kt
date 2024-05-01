package ru.kvf.feature.favorite.folders

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Folder

interface FavoriteFoldersComponent {

    val folders: StateFlow<List<Folder>>

    fun onFolderDoubleClick(id: Long)

    fun onFolderClick(name: String)

    sealed interface Output {
        data class OpenFolderRequested(val name: String) : Output
    }
}
