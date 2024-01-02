package ru.kvf.photos.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import ru.kvf.core.widgets.ImageWithLoader

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotosDetailsScreen(
    viewModel: DetailsViewModel = koinViewModel(),
    photoId: Long
) {
    LaunchedEffect(key1 = Unit, block = { viewModel.start(photoId) })
    val state by viewModel.collectAsState()

    val pagerState = rememberPagerState {
        state.photos.size
    }
    LaunchedEffect(key1 = state.startIndex, block = {
        pagerState.scrollToPage(state.startIndex)
    })

    HorizontalPager(state = pagerState) { index ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            if (state.loading.not()) {
                ImageWithLoader(
                    model = state.photos[index].uri,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}
