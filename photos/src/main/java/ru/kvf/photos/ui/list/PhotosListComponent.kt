package ru.kvf.photos.ui.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PhotosListComponent {
    val state: StateFlow<PhotosListState>
    val sideEffect: Flow<PhotosListSideEffect>

    fun onGridCountClick()
    fun onLikeClick(id: Long)
    fun onReverseClick()
    fun onPhotoClick(photoId: Long)
    fun savePosition(position: Int)

    sealed interface Output {
        data class OpenPhotoRequested(
            val index: Int,
            val reversed: Boolean,
            val folder: String? = null
        ) : Output
    }
}
