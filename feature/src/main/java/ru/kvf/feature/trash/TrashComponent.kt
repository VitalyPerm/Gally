package ru.kvf.feature.trash

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.utils.MediaList

interface TrashComponent {
    fun onMediaClick(id: Long)
    fun onMediaLongClick(id: Long)

    val trash: StateFlow<MediaList>

}