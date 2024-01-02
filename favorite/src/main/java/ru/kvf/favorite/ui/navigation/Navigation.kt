package ru.kvf.favorite.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.kvf.core.ui.details.PhotosDetailsScreen
import ru.kvf.favorite.ui.list.ListScreen

fun NavGraphBuilder.FavoriteNavigation(navController: NavHostController, route: String) {
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
                navArgument(Destinations.PhotoDetails.argument) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Destinations.PhotoDetails.argument) ?: 0
            PhotosDetailsScreen(photoId = id)
        }
    }
}