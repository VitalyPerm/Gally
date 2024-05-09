package ru.kvf.feature.trash

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.MediaList
import ru.kvf.feature.mediabsh.MediaBSHComponent

interface TrashComponent {

    val mediaBSHComponent: MediaBSHComponent

    val media: StateFlow<MediaList>
    val selectedMediaIds: StateFlow<LongSet>
    val gridCount: StateFlow<Int>
    val isReversed: StateFlow<Boolean>
    val sideEffect: Flow<SideEffect>

    fun onMediaClick(id: Long)
    fun onMediaLongClick(id: Long)
    fun onGridCountClick()
    fun onReverseClick()
    fun onSelectMediaDismiss()
    fun selectModeOnShareClick()
    fun selectModeOnUnTrashClick()
    fun selectModeOnDeleteClick()

    sealed interface SideEffect {
        data class UnTrashMedia(val uris: Set<Uri>) : SideEffect
        data class DeleteMedia(val uris: Set<Uri>) : SideEffect
        data class ShareMedia(val media: List<Media>) : SideEffect
    }

    companion object {
        const val DELETE_DAY_FORMAT = "dd MMMM yyyy"
    }
}
