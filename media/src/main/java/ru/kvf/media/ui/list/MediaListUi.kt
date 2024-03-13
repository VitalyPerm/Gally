package ru.kvf.media.ui.list

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
import ru.kvf.core.widgets.MediaListWithDate
import ru.kvf.media.ui.list.delete.DeleteMediaBSH
import ru.kvf.media.R

@Composable
fun MediaListUi(
    component: MediaListComponent,
    isScrollInProgress: MutableState<Boolean>? = null
) {
    val state by component.state.collectAsState()
    val mediaListGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = state.lastPosition
    )
    val ctx = LocalContext.current
    val deleteMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { _ -> }

    LaunchedEffect(mediaListGridState.isScrollInProgress) {
        isScrollInProgress?.value = mediaListGridState.isScrollInProgress
    }

    DisposableEffect(Unit) {
        onDispose { component.savePosition(mediaListGridState.firstVisibleItemIndex) }
    }

    component.sideEffect.collectSideEffect {
        when (it) {
            MediaListSideEffect.ScrollUp -> {
                mediaListGridState.animateScrollToItem(0)
            }

            is MediaListSideEffect.DeleteMedia -> {
                val intentSender = MediaStore.createTrashRequest(
                    ctx.contentResolver,
                    listOf(it.uri),
                    true
                ).intentSender
                val request = IntentSenderRequest.Builder(intentSender)
                    .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
                    .build()
                deleteMediaLauncher.launch(request, ActivityOptionsCompat.makeTaskLaunchBehind())
            }
        }
    }

    DefaultContainer(
        titleRes = R.string.media,
        titleString = state.folderName,
        gridCountActionEnable = true,
        gridCount = state.gridCellsCount,
        onGridCountClick = component::onGridCountClick,
        reverseActionEnable = true,
        onReverseClick = component::onReverseClick
    ) {
        val media = remember(state) {
            with(state) { if (reversed) reversedMedia else media }.toImmutableMap()
        }
        MediaListWithDate(
            media = media,
            likedMedia = state.likedMedia.toImmutableList(),
            gridState = mediaListGridState,
            cellsCount = state.gridCellsCount,
            onMediaClick = component::onMediaClick,
            onLikedClick = component::onLikeClick,
            onMediaLongClick = component::onMediaLongClick
        )
    }

    state.deleteMediaCandidate?.let { media ->
        DeleteMediaBSH(
            media = media,
            onDeleteClick = component::onDeleteMediaClick,
            onDismissClick = component::onDismissDeleteMedia
        )
    }
}
