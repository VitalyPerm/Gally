package ru.kvf.feature.favorite.folders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.widgets.MediaItem

@Composable
fun FavoriteFoldersUi(
    component: FavoriteFoldersComponent
) {
    val folders by component.folders.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        FoldersList(
            folders = folders,
            onFolderClick = component::onFolderClick,
            onFolderDoubleClick = component::onFolderDoubleClick
        )
    }
}

@Composable
private fun FoldersList(
    folders: List<Folder>,
    onFolderClick: (String) -> Unit,
    onFolderDoubleClick: (Long) -> Unit,
) {
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(folders, key = { item: Folder -> item.id }) { folder ->
            MediaItem(
                model = folder.media.firstOrNull()?.uri,
                title = folder.name,
                shouldShowLikeIcon = false,
                onClick = { onFolderClick(folder.name) },
                onDoubleClick = { onFolderDoubleClick(folder.id) }
            )
            MediaItem(
                model = folders,
                favorite = true,
                shouldShowLikeIcon = false,
                onClick = { onFolderClick(folder.name) },
                onDoubleClick = { onFolderDoubleClick(folder.id) },
            )
        }
    }
}
