package ru.kvf.core.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun ImageWithLoader(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    size: Size = Size.ORIGINAL,
    model: Any?
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(model)
            .size(size)
            .build(),
        contentDescription = null,
        contentScale = contentScale,
        loading = {
            Box(
                modifier = Modifier
                    .padding(3.dp)
                    .shimmer(MaterialTheme.colorScheme.inversePrimary)
            )
        },
        modifier = modifier
    )
}
