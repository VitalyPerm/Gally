@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kvf.gally.feature.photos.ui.root

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import ru.kvf.gally.core.widgets.StubScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosRootScreen(
    vm: PhotosRootViewModel = koinViewModel()
) {
    StubScreen(text = "PhotosRootScreen")
}
