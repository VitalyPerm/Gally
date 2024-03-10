package ru.kvf.photos.ui.list

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Photo

interface PhotosListComponent {
    val state: StateFlow<PhotosListState>
    val sideEffect: Flow<PhotosListSideEffect>

    fun onGridCountClick()
    fun onLikeClick(id: Long)
    fun onReverseClick()
    fun onPhotoClick(photoId: Long)
    fun onPhotoLongClick(photo: Photo)
    fun onDeletePhotoClick()
    fun onDismissDeletePhoto()
    fun savePosition(position: Int)

    sealed interface Output {
        data class OpenPhotoRequested(
            val index: Int,
            val reversed: Boolean,
            val folder: String? = null
        ) : Output
    }
}
