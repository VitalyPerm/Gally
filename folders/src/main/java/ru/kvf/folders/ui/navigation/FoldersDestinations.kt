package ru.kvf.folders.ui.navigation

import androidx.navigation.NavBackStackEntry

sealed class FoldersDestinations(val route: String) {
    data object List : FoldersDestinations("folders_list")
    data object FolderDetails : FoldersDestinations("folder_details/{folderName}") {
        fun createRoute(folderName: String) = "folder_details/$folderName"
        fun getFolderName(
            backStackEntry: NavBackStackEntry
        ) = backStackEntry.arguments ?.getString(argument) ?: ""
        const val argument = "folderName"
    }
    data object FolderPhotoDetails : FoldersDestinations("folder_photo_details/{folderName}/{photoId}/{reversed}") {
        fun createRoute(folderName: String, photoId: Long, reversed: Boolean) =
            "folder_photo_details/$folderName/$photoId/$reversed"
        fun getFolderName(
            backStackEntry: NavBackStackEntry
        ) = backStackEntry.arguments ?.getString(folderNameArgument) ?: ""
        fun getPhotoIdArgument(
            backStackEntry: NavBackStackEntry
        ) = backStackEntry.arguments?.getLong(photoIdArgument) ?: 0
        fun getReversedArgument(
            backStackEntry: NavBackStackEntry
        ) = backStackEntry.arguments?.getBoolean(reversedArgument) ?: false
        const val folderNameArgument = "folderName"
        const val photoIdArgument = "photoId"
        const val reversedArgument = "reversed"
    }
}
