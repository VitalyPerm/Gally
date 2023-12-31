package ru.kvf.photos.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.kvf.core.widgets.StubScreen
import ru.kvf.photos.list.ListScreen

fun NavGraphBuilder.PhotosNavigation(navController: NavHostController, route: String) {
    navigation(startDestination = Destinations.List.route, route = route) {
        composable(Destinations.List.route) {
            ListScreen()
        }

        composable(Destinations.PhotoDetails.route) {
            StubScreen(text = Destinations.PhotoDetails.route)
        }
    }
}
