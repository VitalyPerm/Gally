@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kvf.gally.feature.photos.ui.root

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import ru.kvf.gally.App
import ru.kvf.gally.feature.photos.domain.Folder
import ru.kvf.gally.feature.photos.domain.Photo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosRootScreen(
    vm: PhotosRootViewModel = koinViewModel()
) {
    val state by vm.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = "Фото", style = MaterialTheme.typography.headlineLarge) },
            actions = {
                ViewModelIcon(
                    showFolders = state.showFolders,
                    onClick = vm::onChangeViewModeClick
                )
            }
        )
        App.log("folders = ${state.folders}")
        AnimatedContent(targetState = state.showFolders, label = "") { showFolders ->
            if (showFolders) {
                Folders(state.folders)
            } else {
                Photos(state.photos)
            }
        }
    }
}

@Composable
fun ViewModelIcon(
    showFolders: Boolean,
    onClick: () -> Unit
) {
    AnimatedContent(targetState = showFolders, label = "") { folders ->
        IconButton(onClick = onClick) {
            Icon(
                imageVector = if (folders) Icons.Filled.Image else Icons.Filled.Folder,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun Folders(
    folders: List<Folder>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(folders) { folder ->
            AsyncImage(
                model = folder.photos.firstOrNull()?.uri,
                contentDescription = "photo",
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.Low,
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(1.dp)
            )
        }
    }
}

@Composable
private fun Photos(
    photos: List<Photo>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(photos) { photo ->
            AsyncImage(
                model = photo.uri,
                contentDescription = "photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(1.dp)
            )
        }
    }
}
