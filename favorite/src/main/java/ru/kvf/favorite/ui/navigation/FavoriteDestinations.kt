package ru.kvf.favorite.ui.navigation

sealed class FavoriteDestinations(val route: String) {
    data object List : FavoriteDestinations("favorite_list")
    data object PhotoDetails : FavoriteDestinations("favorite_photo_details/{favoritePhotoId}") {
        fun createRoute(photoId: Long) = "favorite_photo_details/$photoId"
        val argument = "favoritePhotoId"
    }
}
