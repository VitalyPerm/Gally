package ru.kvf.media.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import ru.kvf.core.utils.disableFullScreen
import ru.kvf.core.utils.enableFullScreen
import ru.kvf.core.widgets.MediaPager

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaUi(component: MediaComponent) {
    val state by component.state.collectAsState()

    val pagerState = rememberPagerState(initialPage = component.startIndex) {
        state.media.size
    }
    val ctx = LocalContext.current

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect(component::onPageChanged)
    }

    DisposableEffect(Unit) {
        ctx.enableFullScreen()
        onDispose { ctx.disableFullScreen() }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MediaPager(
            media = state.media.toImmutableList(),
            pagerState = pagerState,
            reversePager = state.reversed,
            onTap = component::onSingleTap,
        )

        Title(
            name = state.title,
            titleVisible = state.titleVisible
        )
    }
}

@Composable
private fun Title(
    name: String,
    titleVisible: Boolean
) {
    Column {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        AnimatedVisibility(titleVisible) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                        .padding(start = 8.dp)
                )
            }
        }
    }
}
