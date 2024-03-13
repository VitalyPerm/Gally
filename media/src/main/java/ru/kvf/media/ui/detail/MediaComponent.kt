package ru.kvf.media.ui.detail

import kotlinx.coroutines.flow.StateFlow

interface MediaComponent {
    val state: StateFlow<MediaState>
    val startIndex: Int

    fun onPageChanged(page: Int)
    fun onSingleTap()
}
