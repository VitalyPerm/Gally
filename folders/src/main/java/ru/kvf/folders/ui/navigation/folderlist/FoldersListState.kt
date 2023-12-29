package ru.kvf.folders.ui.navigation.folderlist

import ru.kvf.core.domain.entities.Folder

data class FoldersListState(
    val folders: List<Folder> = emptyList(),
    val gridCellsCount: Int = 1
)
