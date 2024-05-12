package ru.kvf.feature.mediabsh

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.utils.MediaList

interface MediaBSHComponent {

    val media: StateFlow<MediaList>
    val currentMediaIndex: StateFlow<Int>
    val title: StateFlow<String?>
    val deleteDay: StateFlow<String?>
    val optionsVisible: StateFlow<Boolean>
    val isFavorite: StateFlow<Boolean>
    val visible: StateFlow<Boolean>
    val isTrash: Boolean
    val setIndex: Flow<Int>

    fun onTap()
    fun onShareClick()
    fun onTrashClick()
    fun onUnTrashClick()
    fun onFavoriteClick()
    fun onDismissRequest()
    fun setup(startIndex: Int)
    fun onPageChanged(page: Int)
    fun trashedSuccess()
    fun onDeleteClick()
}
