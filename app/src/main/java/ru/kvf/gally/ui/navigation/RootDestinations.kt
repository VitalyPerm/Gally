package ru.kvf.gally.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import ru.kvf.favorite.ui.navigation.FavoriteDestinations
import ru.kvf.folders.ui.navigation.FoldersDestinations
import ru.kvf.photos.ui.navigation.PhotosDestinations
import ru.kvf.settings.ui.navigation.SettingsDestinations

sealed class RootDestinations(
    val route: String,
    val icon: ImageVector
) {
    data object Photos : RootDestinations(
        route = "photos",
        icon = Icons.Filled.Photo
    )
    data object Folders : RootDestinations(
        route = "folders",
        icon = Icons.Filled.Folder
    )
    data object Favorite : RootDestinations(
        route = "favorite",
        icon = Icons.Filled.Favorite
    )
    data object Settings : RootDestinations(
        route = "settings",
        icon = Icons.Filled.Settings
    )
    data object Design : RootDestinations(
        route = "deisgn",
        icon = Icons.Filled.DesignServices
    )

    companion object {
        fun getAll() = listOf(Photos, Folders, Favorite, Settings, Design)
        fun getVisibleNavBarDestinations() = listOf(
            PhotosDestinations.List.route,
            FoldersDestinations.List.route,
            FavoriteDestinations.List.route,
            SettingsDestinations.List.route,
            "deisgn"
        )
    }
}
