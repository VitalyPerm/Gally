package ru.kvf.folders.ui.navigation

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.kvf.folders.ui.navigation.folderlist.FoldersListScreen
import ru.kvf.folders.ui.navigation.folderphotolist.FolderDetailsScreen
import ru.kvf.folders.ui.navigation.photodetail.FolderPhotoDetailsScreen

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
            val folderName = FoldersDestinations.FolderDetails.getFolderName(backStackEntry)
            FolderDetailsScreen(
                folderName = folderName,
                navigateToPhotoDetails = { photoId, reverse ->
                    navController.navigate(
                        FoldersDestinations.FolderPhotoDetails.createRoute(
                            folderName,
                            photoId = photoId,
                            reversed = reverse
                        )
                    )
                }
            )
        }

        composable(
            route = FoldersDestinations.FolderPhotoDetails.route,
            arguments = listOf(
                navArgument(FoldersDestinations.FolderPhotoDetails.folderNameArgument) {
                    type = NavType.StringType
                },
                navArgument(FoldersDestinations.FolderPhotoDetails.photoIdArgument) {
                    type = NavType.LongType
                },
                navArgument(FoldersDestinations.FolderPhotoDetails.reversedArgument) {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
            val folderName = FoldersDestinations.FolderPhotoDetails.getFolderName(backStackEntry)
            val photoId = FoldersDestinations.FolderPhotoDetails.getPhotoIdArgument(backStackEntry)
            val reverse = FoldersDestinations.FolderPhotoDetails.getReversedArgument(backStackEntry)
            FolderPhotoDetailsScreen(
                folderName = folderName,
                photoId = photoId,
                reverse = reverse
            )
        }
    }
}
