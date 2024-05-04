package ru.kvf.feature.mediabsh

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import ru.kvf.core.utils.collectSideEffect
import ru.kvf.core.utils.createTrashMediaRequest
import ru.kvf.core.utils.shareMedia
import ru.kvf.core.widgets.MediaPager
import ru.kvf.core.widgets.MediaSelectModeMenuItem

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MediaBSHUi(
    component: MediaBSHComponent,
    isReversed: Boolean = false,
) {
    val media by component.media.collectAsState()
    val currentMediaIndex by component.currentMediaIndex.collectAsState()
    val title by component.title.collectAsState()
    val optionsVisible by component.optionsVisible.collectAsState()
    val isVisible by component.visible.collectAsState()
    val isFavorite by component.isFavorite.collectAsState()
    val pagerState = rememberPagerState(initialPage = currentMediaIndex) { media.size }

    val ctx = LocalContext.current
    val deleteMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result -> if (result.resultCode == Activity.RESULT_OK) component.trashedSuccess() }

    component.sideEffect.collectSideEffect {
        when (it) {
            is MediaBSHComponent.SideEffect.ShareMedia -> ctx.shareMedia(listOf(it.media))
            is MediaBSHComponent.SideEffect.TrashMedia -> {
                val request = ctx.createTrashMediaRequest(setOf(it.uri))
                deleteMediaLauncher.launch(request, ActivityOptionsCompat.makeTaskLaunchBehind())
            }

            is MediaBSHComponent.SideEffect.SetIndex -> { pagerState.scrollToPage(it.index) }
        }
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

                    Title(
                        name = title,
                        optionsVisible = optionsVisible
                    )

                    if (isFavorite && optionsVisible) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(24.dp)
                        )
                    }

                    Actions(
                        onShareClick = component::onShareClick,
                        onTrashClick = component::onTrashClick,
                        onFavoriteClick = component::onFavoriteClick,
                        isFavorite = isFavorite,
                        optionsVisible = optionsVisible
                    )
                }
            }
        )
    }
}

@Composable
private fun BoxScope.Title(
    name: String,
    optionsVisible: Boolean,
) {
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 48.dp)
    ) {
        AnimatedVisibility(optionsVisible) {
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
fun BoxScope.Actions(
    onTrashClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean,
    optionsVisible: Boolean
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 48.dp)
    ) {
        AnimatedVisibility(optionsVisible) {
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

                MediaSelectModeMenuItem(
                    onClick = onFavoriteClick,
                    imageVector = if (!isFavorite) Icons.Default.Favorite else Icons.Default.HeartBroken
                )
            }
        }
    }
}
