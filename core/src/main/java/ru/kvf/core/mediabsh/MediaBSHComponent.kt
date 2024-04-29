package ru.kvf.core.mediabsh

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media

interface MediaBSHComponent {

    val media: StateFlow<List<Media>>
    val index: StateFlow<Int>
    val title: StateFlow<String>
    val optionsVisible: StateFlow<Boolean>

    fun onTap()
    fun onShareClick()
    fun onTrashClick()
    fun onDismissRequest()

    sealed interface Output {
        data object DismissRequested : Output
    }
}
