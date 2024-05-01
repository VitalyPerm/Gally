package ru.kvf.feature.media

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.feature.mediabsh.MediaBSHComponent
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.MediaDateSet
import ru.kvf.core.utils.MediaMap
import ru.kvf.core.utils.UriSet

interface MediaListComponent {

    val mediaBSHComponent: MediaBSHComponent

    val mediaMap: StateFlow<Pair<MediaMap, MediaMap>>
    val favoriteMediaIds: StateFlow<LongSet>
    val sortReversed: StateFlow<Boolean>
    val gridCellsCount: StateFlow<Int>
    val selectedMediaIds: StateFlow<LongSet>
    val mediaToTrashUris: StateFlow<UriSet>
    val selectedMediaDates: StateFlow<MediaDateSet>
    val sideEffect: Flow<SideEffect>
    val lastPosition: Int
    val folderName: String?

    fun onGridCountClick()
    fun onMediaDoubleClickClick(id: Long)
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

    sealed interface SideEffect {
        data object ScrollUp : SideEffect
        data class TrashMedia(val uris: Set<Uri>) : SideEffect
        data class ShareMedia(val media: List<Media>) : SideEffect
        data object Vibrate : SideEffect
    }
}
