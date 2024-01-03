package ru.kvf.gally.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import ru.kvf.favorite.ui.navigation.FavoriteDestinations
import ru.kvf.gally.R
import ru.kvf.photos.navigation.PhotosDestinations

sealed class RootDestinations(
    val route: String,
    @StringRes val nameRes: Int,
    val icon: ImageVector
) {
    data object Photos : RootDestinations(
        route = "photos",
        nameRes = R.string.root_photos,
        icon = Icons.Filled.Photo
    )
    data object Favorite : RootDestinations(
        route = "favorite",
        nameRes = R.string.root_favorite,
        icon = Icons.Filled.Favorite
    )
    data object Settings : RootDestinations(
        route = "settings",
        nameRes = R.string.root_settings,
        icon = Icons.Filled.Settings
    )
    data object Design : RootDestinations(
        route = "deisgn",
        nameRes = R.string.root_design,
        icon = Icons.Filled.DesignServices
    )

    companion object {
        fun getAll() = listOf(Photos, Favorite, Settings, Design)
        fun getVisibleNavBarDestinations() = listOf(
            PhotosDestinations.List.route,
            FavoriteDestinations.List.route,
            "set",
            "deisgn"
        )
    }
}
