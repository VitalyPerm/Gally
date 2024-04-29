package ru.kvf.favorite.ui.folders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FavoriteFoldersUi(
    component: FavoriteFoldersComponent
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "FavoriteFoldersUi")
    }
}
