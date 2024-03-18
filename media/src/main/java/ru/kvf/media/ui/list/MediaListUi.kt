package ru.kvf.media.ui.list

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.utils.collectSideEffect
import ru.kvf.core.utils.createTrashMediaRequest
import ru.kvf.core.utils.shareMedia
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.core.widgets.MediaListWithDate
import ru.kvf.media.R
import ru.kvf.media.ui.list.delete.TrashMediaBSH

@Composable
fun MediaListUi(
    component: MediaListComponent,
    isScrollInProgress: MutableState<Boolean>? = null,
    selectMediaModeEnable: MutableState<Boolean>? = null
) {
    val selectedMediaIds by component.selectedMediaIds.collectAsState()
    val media by component.media.collectAsState()
    val cellsCount by component.gridCellsCount.collectAsState()
    val sortReversed by component.sortReversed.collectAsState()
    val likedMedia by component.likedMedia.collectAsState()
    val selectedMediaDates by component.selectedMediaDates.collectAsState()
    val mediaToTrashUris by component.mediaToTrashUris.collectAsState()

    val mediaListGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = component.lastPosition
    )
    val ctx = LocalContext.current
    val deleteMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { _ -> }
    val haptic = LocalHapticFeedback.current

    BackHandler(enabled = selectedMediaIds.isNotEmpty()) {
        component.onDismissSelectMedia()
    }

    LaunchedEffect(mediaListGridState.isScrollInProgress) {
        isScrollInProgress?.value = mediaListGridState.isScrollInProgress
    }

    DisposableEffect(Unit) {
        onDispose { component.savePosition(mediaListGridState.firstVisibleItemIndex) }
    }

    LaunchedEffect(selectedMediaIds) {
        selectMediaModeEnable?.value = selectedMediaIds.isNotEmpty()
    }

    component.sideEffect.collectSideEffect {
        when (it) {
            MediaListSideEffect.ScrollUp -> {
                mediaListGridState.animateScrollToItem(0)
            }

            is MediaListSideEffect.DeleteMedia -> {
                val request = ctx.createTrashMediaRequest(it.uris)
                deleteMediaLauncher.launch(request, ActivityOptionsCompat.makeTaskLaunchBehind())
            }

            is MediaListSideEffect.ShareMedia -> {
                ctx.shareMedia(it.media)
            }

            MediaListSideEffect.Vibrate -> haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Content(
        media = media.first,
        reversedMedia = media.second,
        folderName = component.folderName,
        cellsCount = cellsCount,
        gridState = mediaListGridState,
        onGridCountClick = component::onGridCountClick,
        sortReversed = sortReversed,
        onReverseClick = component::onReverseClick,
        likedMedia = likedMedia,
        onMediaClick = component::onMediaClick,
        onMediaLongClick = component::onMediaLongClick,
        onLikedClick = component::onLikeClick,
        selectedMediaIds = selectedMediaIds,
        selectModeOnClickShare = component::selectModeOnClickShare,
        selectModeOnClickTrash = component::selectModeOnClickTrash,
        selectModeOnClickClose = component::onDismissSelectMedia,
        editMode = selectMediaModeEnable?.value ?: false,
        onSelectDateClick = component::onSelectDateClick,
        selectedMediaDates = selectedMediaDates
    )

    TrashMediaBSH(
        media = mediaToTrashUris,
        onDeleteClick = component::onDeleteMediaClick,
        onDismissClick = component::onDismissTrashMedia
    )
}

@Composable
private fun Content(
    media: Map<MediaDate, List<Media>>,
    reversedMedia: Map<MediaDate, List<Media>>,
    folderName: String?,
    cellsCount: Int,
    gridState: LazyGridState,
    onGridCountClick: () -> Unit,
    sortReversed: Boolean,
    onReverseClick: () -> Unit,
    likedMedia: List<Long>,
    onMediaClick: (Long) -> Unit,
    onMediaLongClick: (Media) -> Unit,
    onLikedClick: (Long) -> Unit,
    selectedMediaIds: Set<Long>,
    selectModeOnClickShare: () -> Unit,
    selectModeOnClickClose: () -> Unit,
    selectModeOnClickTrash: () -> Unit,
    onSelectDateClick: (MediaDate) -> Unit,
    selectedMediaDates: Set<MediaDate>,
    editMode: Boolean
) {
    Box {
        DefaultContainer(
            titleRes = R.string.media,
            titleString = folderName,
            gridCountActionEnable = true,
            gridCount = cellsCount,
            onGridCountClick = onGridCountClick,
            reverseActionEnable = true,
            onReverseClick = onReverseClick
        ) {
            val mediaMap = remember(sortReversed) { if (sortReversed) reversedMedia else media }
            MediaListWithDate(
                media = mediaMap,
                likedMedia = likedMedia,
                gridState = gridState,
                cellsCount = cellsCount,
                onMediaClick = onMediaClick,
                onLikedClick = onLikedClick,
                onMediaLongClick = onMediaLongClick,
                selectedMediaIds = selectedMediaIds,
                selectedMediaDates = selectedMediaDates,
                onSelectDateClick = onSelectDateClick
            )
        }

        MediaSelectModeMenu(
            visible = editMode,
            onClickShare = selectModeOnClickShare,
            onClickTrash = selectModeOnClickTrash,
            selectedMediaCount = selectedMediaIds.size,
            onCloseClick = selectModeOnClickClose
        )
    }
}

@Composable
fun BoxScope.MediaSelectModeMenu(
    visible: Boolean,
    onClickShare: () -> Unit,
    onClickTrash: () -> Unit,
    selectedMediaCount: Int,
    onCloseClick: () -> Unit
) {
    var count by remember { mutableIntStateOf(0) }
    LaunchedEffect(selectedMediaCount) {
        if (selectedMediaCount > 0) count = selectedMediaCount
    }
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
    ) {
        AnimatedVisibility(visible) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 48.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onCloseClick,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            modifier = Modifier
                                .size(36.dp)
                        )
                    }

                    Text(
                        text = stringResource(R.string.selected_items_count, count),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .padding(vertical = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                ) {
                    MediaSelectModeMenuItem(
                        onClick = onClickShare,
                        imageVector = Icons.Default.Share
                    )

                    MediaSelectModeMenuItem(
                        onClick = onClickTrash,
                        imageVector = Icons.Default.Delete
                    )
                }
            }
        }
    }
}

@Composable
private fun MediaSelectModeMenuItem(
    imageVector: ImageVector,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .padding(24.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
        )
    }
}
