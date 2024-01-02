@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kvf.photos.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.WifiProtectedSetup
import androidx.compose.material.icons.rounded.HeartBroken
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.size.Size
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.kvf.core.data.CustomDate
import ru.kvf.core.domain.Folder
import ru.kvf.core.domain.Photo
import ru.kvf.core.utils.log
import ru.kvf.core.widgets.ImageWithLoader
import ru.kvf.core.widgets.StubScreen
import ru.kvf.core.widgets.TopBar
import ru.kvf.photos.R

@Composable
fun ListScreen(
    vm: ListViewModel = koinViewModel(),
    navigateToDetails: (Long) -> Unit
) {
    val state by vm.collectAsState()
    val photosListGridState = rememberLazyGridState()

    vm.collectSideEffect {
        when (it) {
            PhotosSideEffect.ScrollUp -> photosListGridState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val title = remember(state.showFolders) {
            if (state.showFolders) R.string.folders else R.string.photos
        }
        TopBar(
            title = stringResource(title),
            actions = {
                AnimatedVisibility(state.showFolders.not()) {
                    ReverseIcon(vm::onReverseIconClick)
                }
                ViewModelIcon(
                    showFolders = state.showFolders,
                    onClick = vm::onChangeViewModeClick
                )
            }
        )
        AnimatedContent(targetState = state.showFolders, label = "") { showFolders ->
            if (showFolders) {
                FoldersList(state.folders)
            } else {
                PhotosList(
                    photos = state.photos,
                    gridState = photosListGridState,
                    onPhotoClick = navigateToDetails
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
    gridState: LazyGridState,
    onPhotoClick: (Long) -> Unit
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
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
            items(photos, key = { item: Photo -> item.id }) { photo ->
                PhotoItem(
                    model = photo.uri,
                    onClick = { onPhotoClick(photo.id) }
                )
            }
        }
    }
}

@Composable
private fun PhotoItem(
    model: Any,
    onClick: () -> Unit,
) {
    var liked by remember { mutableStateOf(false) }
    var showLike by remember { mutableStateOf(false) }
    val hearSize by animateFloatAsState(targetValue = if (showLike) 100f else 0f, label = "")
    LaunchedEffect(key1 = showLike, block = {
        delay(1000)
        showLike = false
    })

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ImageWithLoader(
            model = model,
            contentScale = ContentScale.Crop,
            size = Size(250, 250),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(1.dp)
                .clickable(onClick = onClick)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            showLike = true
                            liked = liked.not()
                            log("double tap detected")
                        },
                        onTap = {
                            onClick()
                        }
                    )
                }
        )

        Icon(
            tint = Color.Red,
            imageVector = if (liked)Icons.Filled.Favorite else Icons.Rounded.HeartBroken,
            contentDescription = null,
            modifier = Modifier
                .size(hearSize.dp)
                .align(Alignment.Center)
        )

        if (liked) {
            Icon(
                tint = Color.Red.copy(alpha = 0.3f),
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(15.dp)
                    .align(Alignment.TopEnd)
            )
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
            FolderItem(
                uri = folder.photos.firstOrNull()?.uri,
                name = folder.name
            )
        }
    }
}

@Composable
fun FolderItem(
    uri: Any?,
    name: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ImageWithLoader(
            model = uri,
            contentScale = ContentScale.Crop,
            size = Size(250, 250),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(6.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Text(
            text = name,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 6.dp)
        )
    }
}
