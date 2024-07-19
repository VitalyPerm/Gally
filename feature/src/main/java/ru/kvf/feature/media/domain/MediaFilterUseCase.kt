package ru.kvf.feature.media.domain

import kotlinx.coroutines.flow.Flow

interface MediaFilterUseCase {
    fun get(): Flow<MediaFilter>
    fun changePhoto()
    fun changeVideo()
}

data class MediaFilter(
    val video: Boolean = true,
    val photo: Boolean = true
) {
    fun all() = video && photo
    fun nothing() = !video && !photo
    fun onlyVideo() = video && !photo
    fun onlyPhoto() = !video && photo
}
