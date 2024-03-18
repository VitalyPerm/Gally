package ru.kvf.media.ui.list

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate

interface MediaListComponent {
    val media: StateFlow<Pair<Map<MediaDate, List<Media>>, Map<MediaDate, List<Media>>>>
    val likedMedia: StateFlow<List<Long>>
    val sortReversed: StateFlow<Boolean>
    val gridCellsCount: StateFlow<Int>
    val selectedMediaIds: StateFlow<Set<Long>>
    val mediaToTrashUris: StateFlow<Set<Uri>>
    val selectedMediaDates: StateFlow<Set<MediaDate>>
    val sideEffect: Flow<MediaListSideEffect>
    val lastPosition: Int
    val folderName: String?

    fun onGridCountClick()
    fun onLikeClick(id: Long)
    fun onReverseClick()
    fun onMediaClick(mediaId: Long)
    fun onMediaLongClick(media: Media)
    fun onDeleteMediaClick()
    fun onDismissTrashMedia()
    fun savePosition(position: Int)
    fun onDismissSelectMedia()
    fun selectModeOnClickShare()
    fun selectModeOnClickTrash()
    fun onSelectDateClick(mediaDate: MediaDate)

    sealed interface Output {
        data class OpenMediaRequested(
            val index: Int,
            val reversed: Boolean,
            val folder: String? = null
        ) : Output
    }
}

sealed interface MediaListSideEffect {
    data object ScrollUp : MediaListSideEffect
    data class DeleteMedia(val uris: Set<Uri>) : MediaListSideEffect
    data class ShareMedia(val media: List<Media>) : MediaListSideEffect
    data object Vibrate : MediaListSideEffect
}
