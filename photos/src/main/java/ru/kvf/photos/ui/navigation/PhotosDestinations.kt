package ru.kvf.photos.ui.navigation

import androidx.navigation.NavBackStackEntry

sealed class PhotosDestinations(val route: String) {

    data object List : PhotosDestinations("photos_list")

    data object PhotoDetails : PhotosDestinations("photo_details/{photoId}/{reverse}") {
        fun createRoute(photoId: Long, reverse: Boolean) = "photo_details/$photoId/$reverse"
        fun getPhotoId(backStackEntry: NavBackStackEntry): Long =
            backStackEntry.arguments?.getLong(photoId) ?: 0
        fun getReverse(backStackEntry: NavBackStackEntry): Boolean =
            backStackEntry.arguments?.getBoolean(reverse) ?: false
        const val photoId = "photoId"
        const val reverse = "reverse"
    }
}
