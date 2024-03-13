package ru.kvf.media.ui.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media

interface MediaListComponent {
    val state: StateFlow<MediaListState>
    val sideEffect: Flow<MediaListSideEffect>

    fun onGridCountClick()
    fun onLikeClick(id: Long)
    fun onReverseClick()
    fun onMediaClick(mediaId: Long)
    fun onMediaLongClick(media: Media)
    fun onDeleteMediaClick()
    fun onDismissDeleteMedia()
    fun savePosition(position: Int)

    sealed interface Output {
        data class OpenMediaRequested(
            val index: Int,
            val reversed: Boolean,
            val folder: String? = null
        ) : Output
    }
}
