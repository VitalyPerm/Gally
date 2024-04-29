package ru.kvf.media.ui.detail

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import ru.kvf.core.utils.collectSideEffect
import ru.kvf.core.utils.createTrashMediaRequest
import ru.kvf.core.utils.shareMedia
import ru.kvf.core.widgets.MediaPager
import ru.kvf.core.widgets.SelectModeMenuItems

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaUi(component: MediaComponent) {
    val media by component.media.collectAsState()
    val title by component.title.collectAsState()
    val optionsVisible by component.optionsVisible.collectAsState()

    val pagerState = rememberPagerState(initialPage = component.startIndex) { media.size }

    val ctx = LocalContext.current
    val deleteMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result -> if (result.resultCode == Activity.RESULT_OK) component.trashedSuccess() }

    component.sideEffect.collectSideEffect {
        when (it) {
            is MediaComponent.SideEffect.ShareMedia -> ctx.shareMedia(listOf(it.media))
            is MediaComponent.SideEffect.TrashMedia -> {
                val request = ctx.createTrashMediaRequest(setOf(it.uri))
                deleteMediaLauncher.launch(request, ActivityOptionsCompat.makeTaskLaunchBehind())
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect(component::onPageChanged)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MediaPager(
            media = media,
            pagerState = pagerState,
            reversePager = component.isReversed,
            onTap = component::onSingleTap
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
