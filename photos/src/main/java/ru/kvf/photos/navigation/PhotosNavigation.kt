package ru.kvf.photos.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.kvf.photos.details.PhotosDetailsScreen
import ru.kvf.photos.folderdetails.FolderDetailsScreen
import ru.kvf.photos.list.PhotosListScreen

fun NavGraphBuilder.photosNavigation(navController: NavHostController, route: String) {
    navigation(startDestination = PhotosDestinations.List.route, route = route) {
        composable(PhotosDestinations.List.route) {
            PhotosListScreen(
                navigateToPhotoDetails = {
                    navController.navigate(PhotosDestinations.PhotoDetails.createRoute(it))
                },
                navigateToFolderDetails = {
                    navController.navigate(PhotosDestinations.FolderDetails.createRoute(it))
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

        composable(
            route = PhotosDestinations.FolderDetails.route,
            arguments = listOf(
                navArgument(PhotosDestinations.FolderDetails.argument) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val folderName = backStackEntry.arguments
                ?.getString(PhotosDestinations.FolderDetails.argument) ?: ""
            FolderDetailsScreen(folderName = folderName)
        }
    }
}
