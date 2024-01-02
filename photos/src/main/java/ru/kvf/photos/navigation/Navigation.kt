package ru.kvf.photos.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.kvf.photos.details.PhotosDetailsScreen
import ru.kvf.photos.list.ListScreen

fun NavGraphBuilder.PhotosNavigation(navController: NavHostController, route: String) {
    navigation(startDestination = Destinations.List.route, route = route) {
        composable(Destinations.List.route) {
            ListScreen(
                navigateToDetails = {
                    navController.navigate(Destinations.PhotoDetails.createRoute(it))
                }
            )
        }

        composable(
            route = Destinations.PhotoDetails.route,
            arguments = listOf(
                navArgument(Destinations.PhotoDetails.getArgument()) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Destinations.PhotoDetails.getArgument()) ?: 0
            PhotosDetailsScreen(photoId = id)
        }
    }
}
