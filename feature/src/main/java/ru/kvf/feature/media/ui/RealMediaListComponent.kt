package ru.kvf.feature.media.ui

import android.content.Context
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.domain.entities.MimeType
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.domain.usecase.ShareMediaUseCase
import ru.kvf.core.domain.usecase.TrashMediaUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaIdsUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFavoriteSetUseCase
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.MediaDateSet
import ru.kvf.core.utils.MediaList
import ru.kvf.core.utils.MediaMap
import ru.kvf.core.utils.collectSafe
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.feature.media.domain.GetSortedMediaUseCase
import ru.kvf.feature.media.domain.MediaFilter
import ru.kvf.feature.media.domain.MediaFilterUseCase

class RealMediaListComponent(
    componentContext: ComponentContext,
    private val onOutput: (MediaListComponent.Output) -> Unit,
    getSortedMediaUseCase: GetSortedMediaUseCase,
    getFavoriteMediaIdsUseCase: GetFavoriteMediaIdsUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
    private val handleFavoriteSetUseCase: HandleFavoriteSetUseCase,
    private val context: Context,
    private val hapticFeedBackUseCase: PerformHapticFeedBackUseCase,
    private val shareMediaUseCase: ShareMediaUseCase,
    private val trashMediaUseCase: TrashMediaUseCase,
    private val mediaFilterUseCase: MediaFilterUseCase
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
    override val scrollUp = MutableSharedFlow<Unit>()
    override var lastPosition = 0

    private val allMedia = MutableStateFlow(MediaList.EMPTY)
    override val videoEnable = mediaFilterUseCase.get().map { it.second }
        .stateIn(componentScope, SharingStarted.Eagerly, true)

    override val photoEnable = mediaFilterUseCase.get().map { it.first }
        .stateIn(componentScope, SharingStarted.Eagerly, true)

    private var allMediaList: List<Media> = emptyList()
    private var mediaDateToIdMap: Map<MediaDate, List<Long>> = emptyMap()

    init {
        componentScope.collectSafe(getSortedMediaUseCase()) { value ->
            allMediaList = value.values.flatten()
            mediaDateToIdMap = value.mapValues { it.value.map(Media::id) }
            updateMedia(value)
        }
    }

    override fun onGridCountClick() {
        componentScope.safeLaunch {
            gridCellsCountChangeUseCase.set(
                value = gridCellsCount.value,
                screen = GridCellsCountChangeUseCase.Screen.MediaList
            )
        }
    }

    override fun onReverseClick() {
        sortReversed.update { it.not() }
        componentScope.launch { scrollUp.emit(Unit) }
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
                val output = when (media.mimeType) {
                    MimeType.Video -> MediaListComponent.Output.VideoRequested(mediaId)
                    MimeType.Photo -> MediaListComponent.Output.MediaDetailsRequested(mediaId)
                }
                onOutput(output)
            }
        }
    }

    override fun savePosition(position: Int) {
        lastPosition = position
    }

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
            shareMediaUseCase(mediaList)
        }
    }

    override fun selectModeOnTrashClick() {
        componentScope.safeLaunch {
            val mediaList = selectedMediaIds.value.data.mapNotNull {
                allMediaList.find { media -> media.id == it }?.uri
            }.toSet()
            selectedMediaIds.update { LongSet.EMPTY }
            trashMediaUseCase(mediaList, true)
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

    override fun onPhotoIconClick() {
        mediaFilterUseCase.set(MediaFilter.Photo)
    }

    override fun onVideoIconClick() {
        mediaFilterUseCase.set(MediaFilter.Video)
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
