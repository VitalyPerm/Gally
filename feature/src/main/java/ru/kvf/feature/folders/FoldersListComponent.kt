package ru.kvf.feature.folders

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.utils.LongSet

interface FoldersListComponent {

    val folders: StateFlow<List<Folder>>
    val favoriteFolderIds: StateFlow<LongSet>
    val gridCellsCount: StateFlow<Int>
    val sideEffect: Flow<SideEffect>

    fun onGridCountClick()
    fun onReverseClick()
    fun onFolderClick(name: String)
    fun onFolderLongClick(id: Long)

    sealed interface Output {
        data class OpenFolderRequested(val name: String) : Output
    }

    sealed interface SideEffect {
        data object Vibrate : SideEffect
    }
}
