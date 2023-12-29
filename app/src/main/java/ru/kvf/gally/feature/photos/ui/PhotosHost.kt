package ru.kvf.gally.feature.photos.ui

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.kvf.gally.core.widgets.StubScreen
import ru.kvf.gally.feature.photos.ui.root.PhotosDestinations
import ru.kvf.gally.feature.photos.ui.root.PhotosRootScreen

fun NavGraphBuilder.PhotosHost(navController: NavHostController, route: String) {
    navigation(startDestination = PhotosDestinations.PhotosRoot.route, route = route) {
        composable(PhotosDestinations.PhotosRoot.route) {
            PhotosRootScreen()
        }

        composable(PhotosDestinations.PhotoDetails.route) {
            StubScreen(text = PhotosDestinations.PhotoDetails.route)
        }
    }
}
