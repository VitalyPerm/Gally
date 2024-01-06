package ru.kvf.folders.ui.navigation

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.kvf.folders.ui.navigation.details.FolderDetailsScreen
import ru.kvf.folders.ui.navigation.list.FoldersListScreen

fun NavGraphBuilder.foldersNavigation(
    navController: NavHostController,
    route: String,
    isScrollInProgress: MutableState<Boolean>
) {
    navigation(startDestination = FoldersDestinations.List.route, route = route) {
        composable(FoldersDestinations.List.route) {
            FoldersListScreen(
                isScrollInProgress = isScrollInProgress,
                navigateToFolderDetails = {
                    navController.navigate(FoldersDestinations.FolderDetails.createRoute(it))
                }
            )
        }

        composable(
            route = FoldersDestinations.FolderDetails.route,
            arguments = listOf(
                navArgument(FoldersDestinations.FolderDetails.argument) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val folderName = backStackEntry.arguments
                ?.getString(FoldersDestinations.FolderDetails.argument) ?: ""
            FolderDetailsScreen(folderName = folderName)
        }
    }
}
