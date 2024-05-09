package ru.kvf.feature.trash

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.utils.MediaList
import ru.kvf.feature.mediabsh.MediaBSHComponent

interface TrashComponent {

    val mediaBSHComponent: MediaBSHComponent

    val media: StateFlow<MediaList>
    val gridCount: StateFlow<Int>
    val isReversed: StateFlow<Boolean>

    fun onMediaClick(id: Long)
    fun onMediaLongClick(id: Long)
    fun onGridCountClick()
    fun onReverseClick()
}
