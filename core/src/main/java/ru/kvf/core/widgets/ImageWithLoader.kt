package ru.kvf.core.widgets

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
        loading = { CircularProgressIndicator() },
        modifier = modifier
    )
}
