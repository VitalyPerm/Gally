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
import kotlinx.coroutines.launch
import ru.kvf.core.ComponentFactory
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.usecase.DeleteMediaUseCase
import ru.kvf.core.domain.usecase.GetTrashMediaUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.domain.usecase.ShareMediaUseCase
import ru.kvf.core.domain.usecase.TrashMediaUseCase
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.MediaList
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.notNegative
import ru.kvf.core.utils.safeLaunch
import ru.kvf.createMediaBSHComponent
import ru.kvf.feature.mediabsh.MediaBSHComponent

class RealTrashComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    getTrashMediaUseCase: GetTrashMediaUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
    private val hapticFeedBackUseCase: PerformHapticFeedBackUseCase,
    private val deleteMediaUseCase: DeleteMediaUseCase,
    private val trashMediaUseCase: TrashMediaUseCase,
    private val shareMediaUseCase: ShareMediaUseCase
) : ComponentContext by componentContext, TrashComponent {

    private val componentScope = coroutineScope()

    override val isReversed = MutableStateFlow(false)

    override val media: StateFlow<MediaList> =
        combine(isReversed, getTrashMediaUseCase()) { isRev, all ->
            if (isRev) all.reversed() else all
        }
            .map(MediaList::from)
            .stateIn(componentScope, SharingStarted.WhileSubscribed(5000), MediaList.EMPTY)

    override val mediaBSHComponent: MediaBSHComponent = componentFactory.createMediaBSHComponent(
        componentContext = childContext("trashMediaBSH"),
        media = media,
        isTrash = true
    )

    override val gridCount = gridCellsCountChangeUseCase
        .get(GridCellsCountChangeUseCase.Screen.Trash)
        .stateIn(componentScope, SharingStarted.Lazily, 1)

    override val selectedMediaIds = MutableStateFlow(LongSet.EMPTY)

    override fun onMediaClick(id: Long) {
        if (selectedMediaIds.value.data.isNotEmpty()) {
            componentScope.safeLaunch {
                hapticFeedBackUseCase()
                selectedMediaIds.value.data.toMutableSet().apply {
                    if (contains(id)) remove(id) else add(id)
                }.let(LongSet::from).let { set -> selectedMediaIds.update { set } }
            }
        } else {
            val index = media.value.data
                .indexOfFirst { it.id == id }.takeIf { it.notNegative() } ?: return
            mediaBSHComponent.setup(index)
        }
    }

    override fun onMediaLongClick(id: Long) {
        if (selectedMediaIds.value.data.isNotEmpty()) return
        componentScope.launch {
            selectedMediaIds.value = LongSet.from(setOf(id))
            hapticFeedBackUseCase()
        }
    }

    override fun onGridCountClick() {
        componentScope.safeLaunch {
            gridCellsCountChangeUseCase.set(
                value = gridCount.value,
                screen = GridCellsCountChangeUseCase.Screen.Trash
            )
        }
    }

    override fun onReverseClick() {
        isReversed.update { !it }
    }

    override fun onSelectMediaDismiss() {
        selectedMediaIds.update { LongSet.EMPTY }
    }

    override fun selectModeOnDeleteClick() {
        componentScope.safeLaunch {
            val mediaUri = getPrepareSelectedMediaList().map { it.uri }.toSet()
            deleteMediaUseCase(mediaUri)
        }
    }

    override fun selectModeOnShareClick() {
        componentScope.safeLaunch { shareMediaUseCase(getPrepareSelectedMediaList()) }
    }

    override fun selectModeOnUnTrashClick() {
        componentScope.safeLaunch {
            val uriSet = getPrepareSelectedMediaList().map { it.uri }.toSet()
            trashMediaUseCase(uriSet, false)
        }
    }

    private fun getPrepareSelectedMediaList(): List<Media> {
        return selectedMediaIds.value.data.mapNotNull {
            media.value.data.find { media -> media.id == it }
        }.also { selectedMediaIds.update { LongSet.EMPTY } }
    }
}
