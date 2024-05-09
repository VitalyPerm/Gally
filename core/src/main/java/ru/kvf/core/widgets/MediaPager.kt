@file:OptIn(ExperimentalFoundationApi::class)

package ru.kvf.core.widgets

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MimeType
import ru.kvf.core.utils.L
import ru.kvf.core.utils.MediaList
import kotlin.math.absoluteValue

@Composable
fun MediaPager(
    modifier: Modifier = Modifier,
    media: MediaList,
    pagerState: PagerState,
    reversePager: Boolean = false,
    onTap: () -> Unit = { },
) {
    PagerContent(
        mediaList = media,
        pagerState = pagerState,
        modifier = modifier,
        reversePager = reversePager,
        onTap = onTap,
    )
}

@Composable
private fun PagerContent(
    modifier: Modifier = Modifier,
    mediaList: MediaList,
    reversePager: Boolean = false,
    pagerState: PagerState,
    onTap: () -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        reverseLayout = reversePager,
        modifier = modifier
    ) { page ->
        val media = mediaList.data[page]
        when (media.mimeType) {
            MimeType.Video -> VideoItem(
                video = media,
                onClick = {
                    L.d("tap!!!")
                    onTap()
                }
            )

            MimeType.Photo -> PhotoItem(
                pagerState = pagerState,
                page = page,
                model = media.uri,
                onTap = { onTap() }
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
) {
    val context = LocalContext.current
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context)
            .build().apply {
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                repeatMode = Player.REPEAT_MODE_ONE
                setMediaItem(MediaItem.fromUri(video.uri))
                prepare()
            }
    }
    DisposableEffect(
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        keepScreenOn = true
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    }
                },
                modifier = Modifier.clickable(onClick = onClick)
            )
        }
    ) {
        onDispose {
            exoPlayer.release()
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
