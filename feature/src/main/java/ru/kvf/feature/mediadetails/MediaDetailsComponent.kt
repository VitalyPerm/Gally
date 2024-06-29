package ru.kvf.feature.mediadetails

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.utils.MediaList

interface MediaDetailsComponent {

    val media: StateFlow<MediaList>
    val type: Type
    val currentMediaIndex: StateFlow<Int?>
    val title: StateFlow<String?>
    val deleteDay: StateFlow<String?>
    val optionsVisible: StateFlow<Boolean>
    val isFavorite: StateFlow<Boolean>
    val isTrash: Boolean

    fun onMediaClick()
    fun onShareClick()
    fun onTrashClick()
    fun onUnTrashClick()
    fun onFavoriteClick()
    fun onPageChanged(page: Int)
    fun trashedSuccess()
    fun onDeleteClick()
    fun onPlayVideoClick()

    sealed interface Type {
        data object All : Type
        data object Favorite : Type
        data object Trash : Type
        data class Folder(val name: String) : Type
    }

    sealed interface Output {
        data class VideoPlayerRequested(val videoId: Long) : Output
    }
}
