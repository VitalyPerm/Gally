package ru.kvf.photos.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.kvf.photos.list.PhotosListScreen
import ru.kvf.photos.details.PhotosDetailsScreen

fun NavGraphBuilder.PhotosNavigation(navController: NavHostController, route: String) {
    navigation(startDestination = PhotosDestinations.List.route, route = route) {
        composable(PhotosDestinations.List.route) {
            PhotosListScreen(
                navigateToDetails = {
                    navController.navigate(PhotosDestinations.PhotoDetails.createRoute(it))
                }
            )
        }

        composable(
            route = PhotosDestinations.PhotoDetails.route,
            arguments = listOf(
                navArgument(PhotosDestinations.PhotoDetails.argument) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(PhotosDestinations.PhotoDetails.argument) ?: 0
            PhotosDetailsScreen(photoId = id)
        }
    }
}
