package ru.kvf.folders.ui.navigation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.size.Size
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.widgets.ImageWithLoader
import ru.kvf.core.widgets.TopBar
import ru.kvf.folders.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldersListScreen(
    isScrollInProgress: MutableState<Boolean>,
    vm: FoldersListViewModel = koinViewModel(),
    navigateToFolderDetails: (String) -> Unit
) {
    val state by vm.collectAsState()
    val photosListGridState = rememberLazyGridState()

    LaunchedEffect(photosListGridState.isScrollInProgress) {
        isScrollInProgress.value = photosListGridState.isScrollInProgress
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                title = stringResource(R.string.folders),
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            FoldersList(
                folders = state.folders.toImmutableList(),
                onFolderClick = navigateToFolderDetails
            )
        }
    }
}

@Composable
private fun FoldersList(
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
private fun FolderItem(
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
            size = Size(500, 500),
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
