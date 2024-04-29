package ru.kvf.favorite.ui.media

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media

interface FavoriteMediaComponent {

    val media: StateFlow<List<Media>>
    val isReversed: StateFlow<Boolean>

    fun onLikeClick(id: Long)
    fun onMediaClick(mediaId: Long)
    fun onReverseClick()
}