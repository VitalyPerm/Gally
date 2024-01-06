package ru.kvf.folders.ui.navigation

sealed class FoldersDestinations(val route: String) {
    data object List : FoldersDestinations("folders_list")
    data object FolderDetails : FoldersDestinations("folder_details/{folderName}") {
        fun createRoute(folderName: String) = "folder_details/$folderName"
        const val argument = "folderName"
    }
}
