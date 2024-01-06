package ru.kvf.folders.ui.navigation.list

import ru.kvf.core.domain.entities.Folder

data class FoldersListState(
    val folders: List<Folder> = emptyList()
)
