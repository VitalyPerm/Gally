package ru.kvf.feature.mediabsh

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.kvf.core.utils.collectOnStart
import ru.kvf.core.widgets.MediaPager
import ru.kvf.core.widgets.MediaSelectModeMenuItem
import ru.kvf.core.widgets.TrashBottomMenu
import ru.kvf.feature.R

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MediaBSHUi(
    component: MediaBSHComponent,
    isReversed: Boolean = false,
) {
    val media by component.media.collectAsState()
    val currentMediaIndex by component.currentMediaIndex.collectAsState()
    val title by component.title.collectAsState()
    val deleteDate by component.deleteDay.collectAsState()
    val isOptionsVisible by component.optionsVisible.collectAsState()
    val isVisible by component.visible.collectAsState()
    val isFavorite by component.isFavorite.collectAsState()
    val pagerState = rememberPagerState(initialPage = currentMediaIndex) { media.data.size }

    component.setIndex.collectOnStart {
        pagerState.scrollToPage(it)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect(component::onPageChanged)
    }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = component::onDismissRequest,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            shape = RectangleShape,
            containerColor = Color.Black,
            scrimColor = Color.Black.copy(alpha = 0.4f),
            windowInsets = WindowInsets(0, 0, 0, 0),
            dragHandle = null,
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    MediaPager(
                        media = media,
                        pagerState = pagerState,
                        reversePager = isReversed,
                        onTap = component::onTap
                    )

                    FavoriteIcon(
                        isFavorite = isFavorite,
                        isOptionsVisible = isOptionsVisible
                    )

                    Title(
                        name = title,
                        isOptionsVisible = isOptionsVisible
                    )

                    Actions(
                        onShareClick = component::onShareClick,
                        onTrashClick = component::onTrashClick,
                        onUnTrashClick = component::onUnTrashClick,
                        onFavoriteClick = component::onFavoriteClick,
                        onDeleteClick = component::onDeleteClick,
                        isFavorite = isFavorite,
                        isOptionsVisible = isOptionsVisible,
                        isTrash = component.isTrash,
                        deleteDate = deleteDate
                    )
                }
            }
        )
    }
}

@Composable
private fun BoxScope.Title(
    name: String,
    isOptionsVisible: Boolean,
) {
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 48.dp)
    ) {
        AnimatedVisibility(isOptionsVisible) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .background(
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        MaterialTheme.shapes.medium
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun BoxScope.FavoriteIcon(
    isFavorite: Boolean,
    isOptionsVisible: Boolean
) {
    AnimatedVisibility(isFavorite && isOptionsVisible) {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier
                    .size(48.dp)
            )
        }
    }
}

@Composable
private fun BoxScope.Actions(
    onTrashClick: () -> Unit,
    onUnTrashClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean,
    isOptionsVisible: Boolean,
    isTrash: Boolean,
    deleteDate: String?
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 48.dp)
    ) {
        AnimatedVisibility(isOptionsVisible) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                deleteDate?.let {
                    Text(
                        text = stringResource(R.string.trash_days_till_delete, it),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                                MaterialTheme.shapes.medium
                            )
                            .padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isTrash) {
                    TrashBottomMenu(
                        onDeleteClick = onDeleteClick,
                        onShareClick = onShareClick,
                        onUnTrashClick = onUnTrashClick
                    )
                } else {
                    StandardBottomMenu(
                        onTrashClick = onTrashClick,
                        onShareClick = onShareClick,
                        onFavoriteClick = onFavoriteClick,
                        isFavorite = isFavorite
                    )
                }
            }
        }
    }
}

@Composable
private fun StandardBottomMenu(
    onTrashClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean,
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
    ) {
        MediaSelectModeMenuItem(
            onClick = onShareClick,
            imageVector = Icons.Default.Share
        )

        MediaSelectModeMenuItem(
            onClick = onTrashClick,
            imageVector = Icons.Default.Delete
        )
        AnimatedContent(targetState = isFavorite, label = "") {
            if (it) {
                MediaSelectModeMenuItem(
                    onClick = onFavoriteClick,
                    imageVector = Icons.Default.HeartBroken
                )
            } else {
                MediaSelectModeMenuItem(
                    onClick = onFavoriteClick,
                    imageVector = Icons.Default.Favorite
                )
            }
        }
    }
}
