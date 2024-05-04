package ru.kvf.feature.mediabsh

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media

interface MediaBSHComponent {

    val media: StateFlow<List<Media>>
    val currentMediaIndex: StateFlow<Int>
    val title: StateFlow<String>
    val optionsVisible: StateFlow<Boolean>
    val isFavorite: StateFlow<Boolean>
    val sideEffect: Flow<SideEffect>
    val visible: StateFlow<Boolean>

    fun onTap()
    fun onShareClick()
    fun onTrashClick()
    fun onFavoriteClick()
    fun onDismissRequest()
    fun setup(startIndex: Int)
    fun onPageChanged(page: Int)
    fun trashedSuccess()

    sealed interface SideEffect {
        data class TrashMedia(val uri: Uri) : SideEffect
        data class ShareMedia(val media: Media) : SideEffect
        data class SetIndex(val index: Int) : SideEffect
    }
}
