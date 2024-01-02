package ru.kvf.favorite.ui.navigation

sealed class Destinations(val route: String) {
    data object List : Destinations("favorite_list")
    data object PhotoDetails : Destinations("favorite_photo_details/{favoritePhotoId}") {
        fun createRoute(photoId: Long) = "favorite_photo_details/$photoId"
        val argument = "favoritePhotoId"
    }
}
