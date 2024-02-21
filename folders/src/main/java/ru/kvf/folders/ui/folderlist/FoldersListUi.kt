package ru.kvf.folders.ui.folderlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.size.Size
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.ImageWithLoader
import ru.kvf.folders.R

@Composable
fun FoldersListUi(
    component: FoldersListComponent,
    navBarPadding: Dp
) {
    val state by component.state.collectAsState()
    val foldersListGridState = rememberLazyGridState()

    DefaultContainer(
        titleRes = R.string.folders,
        gridCountActionEnable = true,
        gridCount = state.gridCellsCount,
        onGridCountClick = component::onGridCountClick,
        modifier = Modifier.padding(bottom = navBarPadding)
    ) {
        FoldersList(
            folders = state.folders.toImmutableList(),
            onFolderClick = component::onFolderClick,
            gridState = foldersListGridState,
            cellsCount = state.gridCellsCount,
        )
    }
}

@Composable
private fun FoldersList(
    folders: ImmutableList<Folder>,
    onFolderClick: (String) -> Unit,
    gridState: LazyGridState,
    cellsCount: Int
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(cellsCount),
        state = gridState,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(folders) { folder ->
            FolderItem(
                uri = folder.photos.firstOrNull()?.uri,
                name = folder.name,
                onClick = { onFolderClick(folder.name) }
            )
        }
    }
}

@Composable
private fun FolderItem(
    uri: Any?,
    name: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .clickable(onClick = onClick)
            .border(
                BorderStroke(3.dp, MaterialTheme.colorScheme.onPrimary),
                MaterialTheme.shapes.large
            )
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageWithLoader(
            model = uri,
            contentScale = ContentScale.Crop,
            size = Size(500, 500),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(6.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 6.dp)
        )
    }
}
