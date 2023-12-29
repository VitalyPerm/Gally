package ru.kvf.photos.ui.navigation

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.kvf.photos.ui.detail.PhotosDetailsScreen
import ru.kvf.photos.ui.list.PhotosListScreen

fun NavGraphBuilder.photosNavigation(
    navController: NavHostController,
    route: String,
    isScrollInProgress: MutableState<Boolean>
) {
    navigation(startDestination = PhotosDestinations.List.route, route = route) {
        composable(PhotosDestinations.List.route) {
            PhotosListScreen(
                isScrollInProgress = isScrollInProgress,
                navigateToPhotoDetails = { photoId, reverse ->
                    navController.navigate(PhotosDestinations.PhotoDetails.createRoute(photoId, reverse))
                }
            )
        }

        composable(
            route = PhotosDestinations.PhotoDetails.route,
            arguments = listOf(
                navArgument(PhotosDestinations.PhotoDetails.photoIndex) {
                    type = NavType.IntType
                },
                navArgument(PhotosDestinations.PhotoDetails.reverse) {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
            val photoId = PhotosDestinations.PhotoDetails.getPhotoId(backStackEntry)
            val reverse = PhotosDestinations.PhotoDetails.getReverse(backStackEntry)
            PhotosDetailsScreen(photoIndex = photoId, reversePager = reverse)
        }
    }
}
