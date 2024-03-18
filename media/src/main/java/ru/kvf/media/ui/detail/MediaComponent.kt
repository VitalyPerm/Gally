package ru.kvf.media.ui.detail

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media

interface MediaComponent {
    val media: StateFlow<List<Media>>
    val titleVisible: StateFlow<Boolean>
    val title: StateFlow<String>
    val startIndex: Int
    val isReversed: Boolean

    fun onPageChanged(page: Int)
    fun onSingleTap()
}
