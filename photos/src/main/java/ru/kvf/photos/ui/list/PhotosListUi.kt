package ru.kvf.photos.ui.list

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityOptionsCompat
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import ru.kvf.core.utils.collectSideEffect
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.PhotosListWithDate
import ru.kvf.photos.R
import ru.kvf.photos.ui.list.delete.DeletePhotoBSH

@Composable
fun PhotosListUi(
    component: PhotosListComponent,
    isScrollInProgress: MutableState<Boolean>? = null
) {
    val state by component.state.collectAsState()
    val photosListGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = state.lastPosition
    )
    val ctx = LocalContext.current
    val deletePhotoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { _ -> }

    LaunchedEffect(photosListGridState.isScrollInProgress) {
        isScrollInProgress?.value = photosListGridState.isScrollInProgress
    }

    DisposableEffect(Unit) {
        onDispose { component.savePosition(photosListGridState.firstVisibleItemIndex) }
    }

    component.sideEffect.collectSideEffect {
        when (it) {
            PhotosListSideEffect.ScrollUp -> {
                photosListGridState.animateScrollToItem(0)
            }

            is PhotosListSideEffect.DeletePhoto -> {
                val intentSender = MediaStore.createTrashRequest(
                    ctx.contentResolver,
                    listOf(it.uri),
                    true
                ).intentSender
                val request = IntentSenderRequest.Builder(intentSender)
                    .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
                    .build()
                deletePhotoLauncher.launch(request, ActivityOptionsCompat.makeTaskLaunchBehind())
            }
        }
    }

    DefaultContainer(
        titleRes = R.string.photos,
        titleString = state.folderName,
        gridCountActionEnable = true,
        gridCount = state.gridCellsCount,
        onGridCountClick = component::onGridCountClick,
        reverseActionEnable = true,
        onReverseClick = component::onReverseClick
    ) {
        val photos = remember(state) {
            with(state) { if (reversed) reversedPhotos else photos }.toImmutableMap()
        }
        PhotosListWithDate(
            photos = photos,
            likedPhotos = state.likedPhotos.toImmutableList(),
            gridState = photosListGridState,
            cellsCount = state.gridCellsCount,
            onPhotoClick = component::onPhotoClick,
            onLikedClick = component::onLikeClick,
            onPhotoLongClick = component::onPhotoLongClick
        )
    }

    state.deletePhotosCandidate?.let { photo ->
        DeletePhotoBSH(
            photo = photo,
            onDeleteClick = component::onDeletePhotoClick,
            onDismissClick = component::onDismissDeletePhoto
        )
    }
}
