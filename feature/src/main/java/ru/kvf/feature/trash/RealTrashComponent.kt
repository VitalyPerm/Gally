package ru.kvf.feature.trash

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.kvf.core.ComponentFactory
import ru.kvf.core.domain.usecase.GetTrashMediaUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.message.MessageComponent
import ru.kvf.core.utils.Constants
import ru.kvf.core.utils.MediaList
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.notNegative
import ru.kvf.core.utils.safeLaunch
import ru.kvf.createMediaBSHComponent
import ru.kvf.feature.R
import ru.kvf.feature.mediabsh.MediaBSHComponent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RealTrashComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    getTrashMediaUseCase: GetTrashMediaUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
    private val messageComponent: MessageComponent
) : ComponentContext by componentContext, TrashComponent {

    private val componentScope = coroutineScope()
    private val daysTillDeleteFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    override val isReversed = MutableStateFlow(false)

    override val media: StateFlow<MediaList> =
        combine(isReversed, getTrashMediaUseCase()) { isRev, all ->
            if (isRev) all.reversed() else all
        }
            .map(MediaList::from)
            .stateIn(componentScope, SharingStarted.WhileSubscribed(5000), MediaList.EMPTY)

    override val mediaBSHComponent: MediaBSHComponent = componentFactory.createMediaBSHComponent(
        componentContext = childContext("trashMediaBSH"),
        media = media
    )

    override val gridCount = gridCellsCountChangeUseCase
        .get(GridCellsCountChangeUseCase.Screen.Trash)
        .stateIn(componentScope, SharingStarted.Lazily, 1)

    override fun onMediaClick(id: Long) {
        val index = media.value.data
            .indexOfFirst { it.id == id }.takeIf { it.notNegative() } ?: return
        mediaBSHComponent.setup(index)
    }

    override fun onMediaLongClick(id: Long) {
        val expiresTimeStamp = media.value.data.find { it.id == id }?.expiresTimeStamp ?: return
        val date = Date(expiresTimeStamp.times(1000))
        val dateString = daysTillDeleteFormat.format(date)
        messageComponent.showMessage(R.string.trash_days_till_delete, dateString)
    }

    override fun onGridCountClick() {
        val currentCount = gridCount.value
        val value =
            if (currentCount == Constants.MAX_GRID_COUNT) Constants.MIN_GRID_COUNT else currentCount + 1
        componentScope.safeLaunch {
            gridCellsCountChangeUseCase.set(
                value = value,
                screen = GridCellsCountChangeUseCase.Screen.Trash
            )
        }
    }

    override fun onReverseClick() {
        isReversed.update { !it }
    }
}
