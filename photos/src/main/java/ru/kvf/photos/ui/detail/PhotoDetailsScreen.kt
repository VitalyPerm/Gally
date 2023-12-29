package ru.kvf.photos.ui.detail

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import ru.kvf.core.utils.findActivity
import ru.kvf.core.widgets.PhotosPager

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotosDetailsScreen(
    photoIndex: Int,
    reversePager: Boolean = false,
    vm: PhotoDetailsViewModel = koinViewModel { parametersOf(photoIndex) }
) {
    val state by vm.collectAsState()
// hide until realisation    ChangeOrientation(state.verticalOrientation)

    val pagerState = rememberPagerState(initialPage = photoIndex) {
        state.photos.size
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect(vm::onPageChanged)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        PhotosPager(
            photos = state.photos.toImmutableList(),
            pagerState = pagerState,
            reversePager = reversePager,
            onTap = vm::onSingleTap
        )

        Title(
            name = state.title,
            titleVisible = state.titleVisible,
            onRotateClick = vm::rotate
        )
    }
}

@Composable
private fun Title(
    name: String,
    titleVisible: Boolean,
    onRotateClick: () -> Unit
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
                /*
                 hide until realisation
                                 IconButton(
                    onClick = onRotateClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.ScreenRotation, contentDescription = null)
                }
                 */
            }
        }
    }
}

@SuppressLint("SourceLockedOrientationActivity")
@Composable
private fun ChangeOrientation(
    trigger: Boolean
) {
    val context = LocalContext.current
    LaunchedEffect(trigger) {
        val activity = context.findActivity()
        val orientation = if (trigger) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        activity?.requestedOrientation = orientation
    }
}
