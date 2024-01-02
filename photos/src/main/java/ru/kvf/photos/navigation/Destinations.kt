package ru.kvf.photos.navigation

sealed class Destinations(val route: String) {
    data object List : Destinations("photos_list")
    data object PhotoDetails : Destinations("photo_details/{photoId}") {
        fun createRoute(photoId: Long) = "photo_details/$photoId"
        val argument = "photoId"
    }
}
