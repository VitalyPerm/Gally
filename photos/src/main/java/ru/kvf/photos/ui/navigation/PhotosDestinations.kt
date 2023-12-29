package ru.kvf.photos.ui.navigation

import androidx.navigation.NavBackStackEntry

sealed class PhotosDestinations(val route: String) {

    data object List : PhotosDestinations("photos_list")

    data object PhotoDetails : PhotosDestinations("photo_details/{photoIndex}/{reverse}") {
        fun createRoute(photoIndex: Int, reverse: Boolean) = "photo_details/$photoIndex/$reverse"
        fun getPhotoId(backStackEntry: NavBackStackEntry): Int =
            backStackEntry.arguments?.getInt(photoIndex) ?: 0
        fun getReverse(backStackEntry: NavBackStackEntry): Boolean =
            backStackEntry.arguments?.getBoolean(reverse) ?: false
        const val photoIndex = "photoIndex"
        const val reverse = "reverse"
    }
}
