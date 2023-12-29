package ru.kvf.favorite.ui.navigation

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.kvf.favorite.ui.details.FavoriteDetailsScreen
import ru.kvf.favorite.ui.list.FavoriteListScreen

fun NavGraphBuilder.favoriteNavigation(
    navController: NavHostController,
    route: String,
    isScrollInProgress: MutableState<Boolean>
) {
    navigation(startDestination = FavoriteDestinations.List.route, route = route) {
        composable(FavoriteDestinations.List.route) {
            FavoriteListScreen(
                navigateToDetails = {
                    navController.navigate(FavoriteDestinations.PhotoDetails.createRoute(it))
                },
                isScrollInProgress = isScrollInProgress
            )
        }

        composable(
            route = FavoriteDestinations.PhotoDetails.route,
            arguments = listOf(
                navArgument(FavoriteDestinations.PhotoDetails.argument) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(FavoriteDestinations.PhotoDetails.argument) ?: 0
            FavoriteDetailsScreen(photoId = id)
        }
    }
}
