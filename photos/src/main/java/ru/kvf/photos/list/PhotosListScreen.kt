
package ru.kvf.photos.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.size.Size
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.widgets.ImageWithLoader
import ru.kvf.core.widgets.PhotosListWithDate
import ru.kvf.core.widgets.ReverseIcon
import ru.kvf.core.widgets.TopBar
import ru.kvf.photos.R

@Composable
fun PhotosListScreen(
    vm: PhotosListViewModel = koinViewModel(),
    isScrollInProgress: MutableState<Boolean>,
    navBarVisible: Boolean,
    navigateToPhotoDetails: (Long) -> Unit,
    navigateToFolderDetails: (String) -> Unit
) {
    val state by vm.collectAsState()
    val photosListGridState = rememberLazyGridState()

    LaunchedEffect(photosListGridState.isScrollInProgress) {
        isScrollInProgress.value = photosListGridState.isScrollInProgress
    }

    vm.collectSideEffect {
        when (it) {
            PhotosListSideEffect.ScrollUp -> photosListGridState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        val title = remember(state.showFolders) {
            if (state.showFolders) R.string.folders else R.string.photos
        }
        AnimatedVisibility(navBarVisible) {
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
        }
        AnimatedContent(targetState = state.showFolders, label = "") { showFolders ->
            if (showFolders) {
                FoldersList(
                    folders = state.folders.toImmutableList(),
                    onFolderClick = navigateToFolderDetails
                )
            } else {
                PhotosListWithDate(
                    photos = state.photos.toImmutableMap(),
                    likedPhotos = state.likedPhotos.toImmutableList(),
                    gridState = photosListGridState,
                    onPhotoClick = navigateToPhotoDetails,
                    onLikedClick = vm::onLikeClick
                )
            }
        }
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

@Composable
fun FoldersList(
    folders: ImmutableList<Folder>,
    onFolderClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
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
fun FolderItem(
    uri: Any?,
    name: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
