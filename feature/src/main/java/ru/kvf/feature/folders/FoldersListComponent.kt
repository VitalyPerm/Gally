package ru.kvf.feature.folders

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.utils.FolderList
import ru.kvf.core.utils.LongSet

interface FoldersListComponent {

    val folders: StateFlow<FolderList>
    val favoriteFolderIds: StateFlow<LongSet>
    val gridCellsCount: StateFlow<Int>

    fun onGridCountClick()
    fun onReverseClick()
    fun onTrashClick()
    fun onFolderClick(name: String)
    fun onFolderLongClick(id: Long)

    sealed interface Output {
        data class OpenFolderRequested(val name: String) : Output
        data object OpenTrashRequested : Output
    }
}
