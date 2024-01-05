package ru.kvf.photos.ui.navigation

sealed class PhotosDestinations(val route: String) {
    data object List : PhotosDestinations("photos_list")
    data object PhotoDetails : PhotosDestinations("photo_details/{photoId}") {
        fun createRoute(photoId: Long) = "photo_details/$photoId"
        const val argument = "photoId"
    }
    data object FolderDetails : PhotosDestinations("folder_details/{folderName}") {
        fun createRoute(folderName: String) = "folder_details/$folderName"
        const val argument = "folderName"
    }
}
