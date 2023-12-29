package ru.kvf.gally.feature.photos.root

sealed class PhotosDestinations(val route: String,) {
    data object PhotosRoot : PhotosDestinations("photos_root")
    data object PhotoDetails : PhotosDestinations("photo_details")
}
