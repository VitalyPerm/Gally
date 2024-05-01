package ru.kvf.feature.favorite.media

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.mediabsh.MediaBSHComponent

interface FavoriteMediaComponent {

    val mediaBSHComponent: MediaBSHComponent

    val media: StateFlow<List<Media>>
    val selectedMediaIndex: StateFlow<Int>
    val isReversed: StateFlow<Boolean>
    val showDetailsBSH: StateFlow<Boolean>

    fun onLikeClick(id: Long)
    fun onMediaClick(mediaId: Long)
    fun onReverseClick()
}
