package ru.kvf.feature.favorite.folders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.utils.FolderList
import ru.kvf.core.widgets.MediaItem

@Composable
fun FavoriteFoldersUi(
    component: FavoriteFoldersComponent,
    cellsCount: Int,
    isReversed: Boolean
) {
    val foldersState by component.folders.collectAsState()
    val folders = remember(isReversed, foldersState) {
        if (isReversed) foldersState.reversed() else foldersState
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        FoldersList(
            folders = folders,
            onFolderClick = component::onFolderClick,
            onFolderLongClick = component::onFolderLongClick,
            cellsCount = cellsCount
        )
    }
}

@Composable
private fun FoldersList(
    folders: FolderList,
    onFolderClick: (String) -> Unit,
    onFolderLongClick: (Long) -> Unit,
    cellsCount: Int
) {
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Fixed(cellsCount),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(folders.data, key = { item: Folder -> item.id }) { folder ->
            val model = remember { folder.media.randomOrNull()?.uri }
            MediaItem(
                model = model,
                title = folder.name,
                onClick = { onFolderClick(folder.name) },
                cellsCount = cellsCount,
                shouldShowFavoriteIcon = false,
                onLongClick = { onFolderLongClick(folder.id) }
            )
        }
    }
}
