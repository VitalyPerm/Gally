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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import kotlinx.collections.immutable.ImmutableList
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import ru.kvf.core.domain.entities.Photo
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotosPager(
    modifier: Modifier = Modifier,
    photos: ImmutableList<Photo>,
    pagerState: PagerState,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseSurface)
    ) { page ->
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
                model = photos[page].uri,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .zoomable(zoomState)
            )

            val visible = page == pagerState.settledPage
            LaunchedEffect(visible) {
                if (visible.not()) zoomState.reset()
            }
        }
    }
}
