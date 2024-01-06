package ru.kvf.photos.ui.navigation

sealed class PhotosDestinations(val route: String) {
    data object List : PhotosDestinations("photos_list")
    data object PhotoDetails : PhotosDestinations("photo_details/{photoId}") {
        fun createRoute(photoId: Long) = "photo_details/$photoId"
        const val argument = "photoId"
    }
}
