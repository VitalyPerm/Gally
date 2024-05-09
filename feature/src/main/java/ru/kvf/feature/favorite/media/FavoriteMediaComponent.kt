package ru.kvf.feature.favorite.media

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.utils.MediaList
import ru.kvf.feature.mediabsh.MediaBSHComponent

interface FavoriteMediaComponent {

    val mediaBSHComponent: MediaBSHComponent

    val media: StateFlow<MediaList>
    val selectedMediaIndex: StateFlow<Int>
    val showDetailsBSH: StateFlow<Boolean>

    fun onMediaClick(mediaId: Long)
    fun onMediaLongClick(mediaId: Long)
}
