package ru.kvf.feature.media

import android.content.Context
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kvf.core.ComponentFactory
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.domain.usecase.GetFolderMediaUseCase
import ru.kvf.core.domain.usecase.GetSortedMediaUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaIdsUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFavoriteSetUseCase
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.MediaDateSet
import ru.kvf.core.utils.MediaList
import ru.kvf.core.utils.MediaMap
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.createMediaBSHComponent
import ru.kvf.feature.mediabsh.MediaBSHComponent

class RealMediaListComponent(
    componentContext: ComponentContext,
    override val folderName: String? = null,
    getSortedMediaUseCase: GetSortedMediaUseCase,
    getFolderMediaUseCase: GetFolderMediaUseCase,
    getFavoriteMediaIdsUseCase: GetFavoriteMediaIdsUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
    private val handleFavoriteSetUseCase: HandleFavoriteSetUseCase,
    componentFactory: ComponentFactory,
    private val context: Context,
    private val hapticFeedBackUseCase: PerformHapticFeedBackUseCase
) : ComponentContext by componentContext, MediaListComponent {

    private val componentScope = coroutineScope()

    override val gridCellsCount = gridCellsCountChangeUseCase
        .get(GridCellsCountChangeUseCase.Screen.MediaList)
        .stateIn(componentScope, SharingStarted.Lazily, 1)

    override val mediaMap = MutableStateFlow(MediaMap.EMPTY to MediaMap.EMPTY)
    override val favoriteMediaIds: StateFlow<LongSet> = getFavoriteMediaIdsUseCase()
        .stateIn(componentScope, SharingStarted.WhileSubscribed(5000), LongSet.EMPTY)
    override val sortReversed = MutableStateFlow(false)
    override val selectedMediaIds = MutableStateFlow(LongSet.EMPTY)
    override val selectedMediaDates = MutableStateFlow(MediaDateSet.EMPTY)
    override var lastPosition = 0
    override val sideEffect = MutableSharedFlow<MediaListComponent.SideEffect>()

    private val allMedia = MutableStateFlow(MediaList.EMPTY)
    override val mediaBSHComponent: MediaBSHComponent = componentFactory.createMediaBSHComponent(
        componentContext = childContext("mediaListBSH"),
        media = allMedia
    )

    private var allMediaList: List<Media> = emptyList()
    private var mediaDateToIdMap: Map<MediaDate, List<Long>> = emptyMap()

    init {
        if (folderName != null) {
            componentScope.collectFlow(getFolderMediaUseCase.sorted(folderName)) { media ->
                updateMedia(media)
            }
        } else {
            componentScope.collectFlow(getSortedMediaUseCase()) { value ->
                allMediaList = value.values.flatten()
                mediaDateToIdMap = value.mapValues { it.value.map(Media::id) }
                updateMedia(value)
            }
        }
    }

    override fun onGridCountClick() {
        componentScope.safeLaunch {
            val value = if (gridCellsCount.value == 4) 1 else gridCellsCount.value + 1
            gridCellsCountChangeUseCase.set(
                value = value,
                screen = GridCellsCountChangeUseCase.Screen.MediaList
            )
        }
    }

    override fun onReverseClick() {
        sortReversed.update { it.not() }
        componentScope.launch { sideEffect.emit(MediaListComponent.SideEffect.ScrollUp) }
    }

    override fun onMediaClick(mediaId: Long) {
        componentScope.safeLaunch {
            if (selectedMediaIds.value.data.isNotEmpty()) {
                editSelectedMedia(mediaId)
            } else {
                val media = allMedia.value.data.find { it.id == mediaId } ?: return@safeLaunch
                context.imageLoader.execute(
                    ImageRequest.Builder(context)
                        .data(media.uri)
                        .size(Size.ORIGINAL)
                        .build()
                )
                val index = allMedia.value.data.indexOf(media)
                mediaBSHComponent.setup(index)
            }
        }
    }

    override fun savePosition(position: Int) { lastPosition = position }

    override fun onMediaLongClick(media: Media) {
        if (selectedMediaIds.value.data.isNotEmpty()) return
        componentScope.launch {
            selectedMediaIds.value = LongSet.from(setOf(media.id))
            hapticFeedBackUseCase()
        }
    }

    override fun onSelectMediaDismiss() {
        selectedMediaIds.value = LongSet.EMPTY
        selectedMediaDates.value = MediaDateSet.EMPTY
    }

    override fun selectModeOnShareClick() {
        componentScope.launch {
            val mediaList = selectedMediaIds.value.data.mapNotNull {
                allMediaList.find { media -> media.id == it }
            }
            selectedMediaIds.value = LongSet.EMPTY
            sideEffect.emit(MediaListComponent.SideEffect.ShareMedia(mediaList))
        }
    }

    override fun selectModeOnTrashClick() {
        componentScope.safeLaunch {
            val mediaList = selectedMediaIds.value.data.mapNotNull {
                allMediaList.find { media -> media.id == it }?.uri
            }.toSet()
            selectedMediaIds.update { LongSet.EMPTY }
            sideEffect.emit(MediaListComponent.SideEffect.TrashMedia(mediaList))
        }
    }

    override fun selectModeOnFavoriteClick() {
        componentScope.safeLaunch {
            handleFavoriteSetUseCase(selectedMediaIds.value, true)
            selectedMediaIds.update { LongSet.EMPTY }
        }
    }

    override fun selectModeOnDisFavoriteClick() {
        componentScope.safeLaunch {
            handleFavoriteSetUseCase(selectedMediaIds.value, false)
            selectedMediaIds.update { LongSet.EMPTY }
        }
    }

    override fun onSelectDateClick(mediaDate: MediaDate) {
        selectedMediaIds.update { value ->
            val newSet = value.data.toMutableSet().apply {
                val newSelectedMediaIds = allMediaList.filter { it.date == mediaDate }
                    .map(Media::id).toSet()
                val wasAlreadySelected = mediaDate in selectedMediaDates.value.data
                if (wasAlreadySelected) {
                    removeAll(newSelectedMediaIds)
                } else {
                    addAll(newSelectedMediaIds)
                }
            }
            LongSet.from(newSet)
        }
        selectedMediaDates.update { value ->
            val newValue = value.data.toMutableSet().apply {
                if (contains(mediaDate)) remove(mediaDate) else add(mediaDate)
            }
            MediaDateSet.from(newValue)
        }
    }

    private fun updateMedia(data: Map<MediaDate, List<Media>>) {
        val normalMap = MediaMap.from(data.mapValues { MediaList.from(it.value) })
        val reversedMap = MediaMap.from(data.mapValues { MediaList.from(it.value) }.toSortedMap())
        mediaMap.update { normalMap to reversedMap }
        allMedia.update { MediaList.from(data.values.flatten()) }
    }

    private fun editSelectedMedia(id: Long) {
        componentScope.safeLaunch(Dispatchers.Default) {
            hapticFeedBackUseCase()
            val value = selectedMediaIds.value.data.toMutableList().apply {
                if (contains(id)) remove(id) else add(id)
            }.toSet()
            selectedMediaIds.value = LongSet.from(value)
            checkAllMediaOfDaySelected(id)
        }
    }
    private fun checkAllMediaOfDaySelected(id: Long) {
        componentScope.safeLaunch(Dispatchers.Default) {
            val dateToCheck = allMediaList.find { it.id == id }?.date ?: return@safeLaunch
            val isAllMediaSelected = allMediaList.filter { it.date == dateToCheck }.map { it.id }
                .all { it in selectedMediaIds.value.data }
            selectedMediaDates.update { value ->
                val newValue = value.data.toMutableSet().apply {
                    if (isAllMediaSelected) add(dateToCheck) else remove(dateToCheck)
                }
                MediaDateSet.from(newValue)
            }
        }
    }
}
