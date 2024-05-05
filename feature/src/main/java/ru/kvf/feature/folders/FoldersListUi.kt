package ru.kvf.feature.folders

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import ru.kvf.core.utils.FolderList
import ru.kvf.core.utils.LongSet
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.MediaItem
import ru.kvf.feature.R

@Composable
fun FoldersListUi(
    component: FoldersListComponent,
    navBarPadding: Dp
) {
    val folders by component.folders.collectAsState()
    val favoriteFolderIds by component.favoriteFolderIds.collectAsState()
    val gridCellsCount by component.gridCellsCount.collectAsState()
    val foldersListGridState = rememberLazyGridState()

    DefaultContainer(
        titleRes = R.string.folders,
        gridCount = gridCellsCount,
        onGridCountClick = component::onGridCountClick,
        onReverseClick = component::onReverseClick,
        onTrashClick = component::onTrashClick,
        modifier = Modifier.padding(bottom = navBarPadding)
    ) {
        FoldersList(
            folders = folders,
            onFolderClick = component::onFolderClick,
            onFolderLongClick = component::onFolderLongClick,
            gridState = foldersListGridState,
            cellsCount = gridCellsCount,
            favoriteFolderIds = favoriteFolderIds
        )
    }
}

@Composable
private fun FoldersList(
    folders: FolderList,
    onFolderClick: (String) -> Unit,
    onFolderLongClick: (Long) -> Unit,
    gridState: LazyGridState,
    cellsCount: Int,
    favoriteFolderIds: LongSet
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(cellsCount),
        state = gridState,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(folders.data, key = { it.id }) { folder ->
            val model = remember { folder.media.randomOrNull()?.uri }
            MediaItem(
                model = model,
                title = folder.name,
                favorite = folder.id in favoriteFolderIds.data,
                onClick = { onFolderClick(folder.name) },
                onLongClick = { onFolderLongClick(folder.id) },
                cellsCount = cellsCount
            )
        }
    }
}
