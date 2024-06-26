package ru.kvf.feature.media.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.MediaDateSet
import ru.kvf.core.utils.MediaMap

interface MediaListComponent {

    val mediaMap: StateFlow<Pair<MediaMap, MediaMap>>
    val favoriteMediaIds: StateFlow<LongSet>
    val sortReversed: StateFlow<Boolean>
    val gridCellsCount: StateFlow<Int>
    val selectedMediaIds: StateFlow<LongSet>
    val selectedMediaDates: StateFlow<MediaDateSet>
    val photoEnable: StateFlow<Boolean>
    val videoEnable: StateFlow<Boolean>
    val scrollUp: Flow<Unit>
    val lastPosition: Int

    fun onGridCountClick()
    fun onReverseClick()
    fun onMediaClick(mediaId: Long)
    fun onMediaLongClick(media: Media)
    fun savePosition(position: Int)
    fun onSelectMediaDismiss()
    fun selectModeOnShareClick()
    fun selectModeOnTrashClick()
    fun selectModeOnFavoriteClick()
    fun selectModeOnDisFavoriteClick()
    fun onSelectDateClick(mediaDate: MediaDate)
    fun onPhotoIconClick()
    fun onVideoIconClick()

    sealed interface Output {
        data class MediaDetailsRequested(val mediaId: Long) : Output
    }
}
