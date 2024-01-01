@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kvf.photos.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.WifiProtectedSetup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.size.Size
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import ru.kvf.core.data.CustomDate
import ru.kvf.core.domain.Folder
import ru.kvf.core.domain.Photo
import ru.kvf.core.utils.log
import ru.kvf.core.widgets.ImageWithLoader
import ru.kvf.core.widgets.StubScreen
import ru.kvf.core.widgets.TopBar

@Composable
fun ListScreen(
    vm: ListViewModel = koinViewModel()
) {
    val state by vm.collectAsState()
    val photosListGridState = rememberLazyGridState()

    LaunchedEffect(key1 = state.reversed) {
        photosListGridState.animateScrollToItem(0)
    }
    log("${state.photos.keys}")
    log("${state.reversed}")

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBar(
            title = "Фото",
            actions = {
                ViewModelIcon(
                    showFolders = state.showFolders,
                    onClick = vm::onChangeViewModeClick
                )
                ReverseIcon(vm::onReverseIconClick)
            }
        )
        AnimatedContent(targetState = state.showFolders, label = "") { showFolders ->
            if (showFolders) {
                FoldersList(state.folders)
            } else {
                PhotosList(
                    photos = state.photos,
                    gridState = photosListGridState
                )
            }
        }
    }
}

@Composable
private fun ReverseIcon(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.WifiProtectedSetup,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun ViewModelIcon(
    showFolders: Boolean,
    onClick: () -> Unit
) {
    AnimatedContent(targetState = showFolders, label = "") { folders ->
        IconButton(onClick = onClick) {
            Icon(
                imageVector = if (folders) Icons.Filled.Image else Icons.Filled.Folder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    StubScreen(text = "hello")
}

@Composable
fun PhotosList(
    photos: Map<CustomDate, List<Photo>>,
    gridState: LazyGridState
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        photos.forEach { (date, photos) ->
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = date.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(photos, key = { item: Photo -> item.id }) { photo ->
                ImageWithLoader(
                    model = photo.uri,
                    contentScale = ContentScale.Crop,
                    size = Size(250, 250),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@Composable
fun FoldersList(
    folders: List<Folder>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(folders) { folder ->
            ImageWithLoader(
                model = folder.photos.firstOrNull()?.uri,
                contentScale = ContentScale.Crop,
                size = Size(250, 250),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }
    }
}
