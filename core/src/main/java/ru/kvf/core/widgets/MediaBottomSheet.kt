package ru.kvf.core.widgets

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import ru.kvf.core.mediabsh.MediaBSHComponent
import ru.kvf.core.utils.navigationBarWithImePaddingDp

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MediaBottomSheet(
    component: MediaBSHComponent,
    isReversed: Boolean = false,
) {
    val media by component.media.collectAsState()
    val title by component.title.collectAsState()
    val index by component.index.collectAsState()
    val optionsVisible by component.optionsVisible.collectAsState()
    val pagerState = rememberPagerState(initialPage = index) { media.size }
    val navigationBarWithImePadding = navigationBarWithImePaddingDp()
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
                    .padding(navigationBarWithImePadding)
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

                Actions(
                    onShareClick = component::onShareClick,
                    onTrashClick = component::onTrashClick,
                    optionsVisible = optionsVisible
                )
            }
        }
    )
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
    optionsVisible: Boolean
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 48.dp)
    ) {
        AnimatedVisibility(optionsVisible) {
            SelectModeMenuItems(onShareClick = onShareClick, onTrashClick = onTrashClick)
        }
    }
}
