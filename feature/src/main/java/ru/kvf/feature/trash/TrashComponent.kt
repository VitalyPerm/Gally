package ru.kvf.feature.trash

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.MediaList

interface TrashComponent {

    val media: StateFlow<MediaList>
    val selectedMediaIds: StateFlow<LongSet>
    val gridCount: StateFlow<Int>
    val isReversed: StateFlow<Boolean>

    fun onMediaClick(mediaId: Long)
    fun onMediaLongClick(id: Long)
    fun onGridCountClick()
    fun onReverseClick()
    fun onSelectMediaDismiss()
    fun selectModeOnShareClick()
    fun selectModeOnUnTrashClick()
    fun selectModeOnDeleteClick()

    sealed interface Output {
        data class MediaDetailsRequested(val mediaId: Long) : Output
    }

    companion object {
        const val DELETE_DAY_FORMAT = "dd MMMM yyyy"
    }
}
