package ru.kvf.feature.trash

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.core.utils.L
import ru.kvf.core.utils.MediaList
import ru.kvf.core.utils.coroutineScope

class RealTrashComponent(
    componentContext: ComponentContext,
    mediaRepository: MediaRepository
) : ComponentContext by componentContext, TrashComponent {

    private val componentScope = coroutineScope()

    override val media: StateFlow<MediaList> = MutableStateFlow(MediaList.EMPTY)

    val a = mediaRepository.trashFlow
        .stateIn(componentScope, SharingStarted.Eagerly, emptyList())

    init {
        a.onEach { L.d("size = ${it.size}") }.launchIn(componentScope)
    }
}
