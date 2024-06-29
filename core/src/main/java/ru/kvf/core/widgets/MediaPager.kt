@file:OptIn(ExperimentalFoundationApi::class)

package ru.kvf.core.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MimeType
import ru.kvf.core.utils.MediaList
import kotlin.math.absoluteValue

@Composable
fun MediaPager(
    modifier: Modifier = Modifier,
    media: MediaList,
    pagerState: PagerState,
    onClick: () -> Unit = { },
    onPlayVideoClick: () -> Unit,
) {
    PagerContent(
        mediaList = media,
        pagerState = pagerState,
        modifier = modifier,
        onClick = onClick,
        onPlayVideoClick = onPlayVideoClick
    )
}

@Composable
private fun PagerContent(
    modifier: Modifier = Modifier,
    mediaList: MediaList,
    pagerState: PagerState,
    onClick: () -> Unit,
    onPlayVideoClick: () -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        val media = mediaList.data[page]
        when (media.mimeType) {
            MimeType.Video -> VideoItem(
                video = media,
                onClick = onClick,
                onPlayClick = onPlayVideoClick
            )

            MimeType.Photo -> PhotoItem(
                pagerState = pagerState,
                page = page,
                model = media.uri,
                onTap = { onClick() }
            )
        }
    }
}

@SuppressLint("OpaqueUnitKey")
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun VideoItem(
    video: Media,
    onClick: () -> Unit,
    onPlayClick: () -> Unit,
) {
    Box {
        ImageWithLoader(
            model = video.uri,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim)
                .clickable(onClick = onClick)
        )

        IconButton(
            onClick = onPlayClick,
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
            )
        }

        video.duration?.let {
            Text(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = it,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(32.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    .padding(horizontal = 3.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun PhotoItem(
    pagerState: PagerState,
    page: Int,
    model: Any,
    onTap: (Offset) -> Unit
) {
    val zoomState = rememberZoomState()
    Card(
        modifier = Modifier
            .graphicsLayer {
                val direction = pagerState.currentPage - page
                val pageOffset =
                    (direction + pagerState.currentPageOffsetFraction).absoluteValue

                val scale = androidx.compose.ui.layout.lerp(
                    start = ScaleFactor(0.9f, 0.9f),
                    stop = ScaleFactor(1f, 1f),
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )

                scaleX = scale.scaleX
                scaleY = scale.scaleY
                translationX = (1 - scale.scaleX) * direction * size.width / 2f
            }
    ) {
        ImageWithLoader(
            model = model,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim)
                .zoomable(
                    zoomState = zoomState,
                    onTap = onTap
                )
        )

        val visible = page == pagerState.settledPage
        LaunchedEffect(visible) {
            if (visible.not()) zoomState.reset()
        }
    }
}
