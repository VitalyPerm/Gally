package ru.kvf.photos.ui.detail

import kotlinx.coroutines.flow.StateFlow

interface PhotoComponent {
    val state: StateFlow<PhotoState>
    val startIndex: Int

    fun onPageChanged(page: Int)
    fun onSingleTap()
}
