package ru.kvf.media.ui.detail

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media
import ru.kvf.media.ui.list.MediaListComponent

interface MediaComponent {
    val media: StateFlow<List<Media>>
    val optionsVisible: StateFlow<Boolean>
    val title: StateFlow<String>
    val sideEffect: Flow<SideEffect>
    val startIndex: Int
    val isReversed: Boolean

    fun onPageChanged(page: Int)
    fun onSingleTap()
    fun onShareClick()
    fun onTrashClick()
    fun trashedSuccess()

    data class Config(
        val startIndex: Int,
        val isReversed: Boolean,
        val isFavoriteOnly: Boolean,
        val folder: String?
    )

    sealed interface SideEffect {
        data class TrashMedia(val uri: Uri) : SideEffect
        data class ShareMedia(val media: Media) : SideEffect
    }
}
