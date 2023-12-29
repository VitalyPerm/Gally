@file:OptIn(ExperimentalFoundationApi::class)

package ru.kvf.core.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.util.lerp
import kotlinx.collections.immutable.ImmutableList
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import ru.kvf.core.domain.entities.Photo
import kotlin.math.absoluteValue

@Composable
fun PhotosPager(
    modifier: Modifier = Modifier,
    photos: ImmutableList<Photo>,
    pagerState: PagerState,
    reversePager: Boolean = false,
    onTap: () -> Unit = { }
) {
    PagerContent(
        photos = photos,
        pagerState = pagerState,
        modifier = modifier,
        reversePager = reversePager,
        onTap = onTap
    )
}

@Composable
private fun PagerContent(
    modifier: Modifier = Modifier,
    photos: ImmutableList<Photo>,
    reversePager: Boolean = false,
    pagerState: PagerState,
    onTap: () -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        reverseLayout = reversePager,
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseSurface)
    ) { page ->
        val photo = photos[page]
        PagerContentItem(
            pagerState = pagerState,
            page = page,
            model = photo.uri,
            onTap = { onTap() }
        )
    }
}

@Composable
fun PagerContentItem(
    pagerState: PagerState,
    page: Int,
    model: Any,
    onTap: (Offset) -> Unit
) {
    val zoomState = rememberZoomState()
    Card(
        modifier = Modifier
            .graphicsLayer {
                val pageOffset = (
                    (pagerState.currentPage - page) +
                        pagerState.currentPageOffsetFraction
                    )

                alpha = lerp(
                    start = 0.4f,
                    stop = 1f,
                    fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                )

                cameraDistance = 8 * density
                rotationY = lerp(
                    start = 0f,
                    stop = 40f,
                    fraction = pageOffset.coerceIn(-1f, 1f),
                )

                lerp(
                    start = 0.5f,
                    stop = 1f,
                    fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                ).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }
            }
    ) {
        ImageWithLoader(
            model = model,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inversePrimary)
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
