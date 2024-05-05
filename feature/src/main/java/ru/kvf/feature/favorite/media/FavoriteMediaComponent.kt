package ru.kvf.feature.favorite.media

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media
import ru.kvf.feature.mediabsh.MediaBSHComponent

interface FavoriteMediaComponent {

    val mediaBSHComponent: MediaBSHComponent

    val media: StateFlow<List<Media>>
    val selectedMediaIndex: StateFlow<Int>
    val showDetailsBSH: StateFlow<Boolean>

    fun onMediaClick(mediaId: Long)
    fun onMediaLongClick(mediaId: Long)
}
