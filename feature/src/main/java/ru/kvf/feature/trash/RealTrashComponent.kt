package ru.kvf.feature.trash

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.kvf.core.domain.usecase.GetTrashMediaUseCase
import ru.kvf.core.utils.MediaList
import ru.kvf.core.utils.coroutineScope

class RealTrashComponent(
    componentContext: ComponentContext,
    getTrashMediaUseCase: GetTrashMediaUseCase
) : ComponentContext by componentContext, TrashComponent {

    private val componentScope = coroutineScope()

    override val trash: StateFlow<MediaList> = getTrashMediaUseCase().map(MediaList::from)
        .stateIn(componentScope, SharingStarted.WhileSubscribed(5000), MediaList.EMPTY)

    override fun onMediaClick(id: Long) {
    }

    override fun onMediaLongClick(id: Long) {
    }
}
