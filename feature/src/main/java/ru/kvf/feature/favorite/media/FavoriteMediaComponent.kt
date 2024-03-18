package ru.kvf.feature.favorite.media

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.utils.MediaList

interface FavoriteMediaComponent {

    val media: StateFlow<MediaList>
    val selectedMediaIndex: StateFlow<Int>
    val showDetailsBSH: StateFlow<Boolean>

    fun onMediaClick(mediaId: Long)
    fun onMediaLongClick(mediaId: Long)

    sealed interface Output {
        data class MediaDetailsRequested(val mediaId: Long) : Output
    }
}
