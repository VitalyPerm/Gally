package ru.kvf.media.ui.list

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.media.domain.GetFolderMediaUseCase
import ru.kvf.media.domain.GetSortedMediaUseCase

class RealMediaListComponent(
    componentContext: ComponentContext,
    private val onOutput: (MediaListComponent.Output) -> Unit,
    override val folderName: String? = null,
    getSortedMediaUseCase: GetSortedMediaUseCase,
    private val getFolderMediaUseCase: GetFolderMediaUseCase,
    getLikedIdsListUseCase: GetLikedIdsListUseCase,
    private val getMediaUseCase: GetMediaUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase
) : ComponentContext by componentContext, MediaListComponent {

    private val componentScope = lifecycle.coroutineScope()

    override val gridCellsCount = gridCellsCountChangeUseCase
        .get(GridCellsCountChangeUseCase.Screen.MediaList)
        .stateIn(componentScope, SharingStarted.Lazily, 1)
    override val media = MutableStateFlow(
        mapOf<MediaDate, List<Media>>() to
            mapOf<MediaDate, List<Media>>()
    )
    override val likedMedia = getLikedIdsListUseCase()
        .stateIn(componentScope, SharingStarted.Lazily, emptyList())
    override val sortReversed = MutableStateFlow(false)
    override val selectedMediaIds = MutableStateFlow(emptySet<Long>())
    override val mediaToTrashUris = MutableStateFlow(emptySet<Uri>())
    override val selectedMediaDates = MutableStateFlow(emptySet<MediaDate>())
    override var lastPosition = 0
    override val sideEffect = MutableSharedFlow<MediaListSideEffect>()

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

    override fun onLikeClick(id: Long) {
        componentScope.safeLaunch { handleLikeClickUseCase(id) }
    }

    override fun onReverseClick() {
        sortReversed.update { it.not() }
        componentScope.launch { sideEffect.emit(MediaListSideEffect.ScrollUp) }
    }

    override fun onMediaClick(mediaId: Long) {
        componentScope.safeLaunch {
            if (selectedMediaIds.value.isNotEmpty()) {
                editSelectedMedia(mediaId)
            } else {
                if (folderName != null) {
                    getFolderMediaUseCase(folderName).firstOrNull()
                } else {
                    getMediaUseCase().firstOrNull()
                }?.let { list ->
                    val index = list.indexOfFirst { it.id == mediaId }
                    onOutput(
                        MediaListComponent.Output.OpenMediaRequested(
                            index = index,
                            reversed = sortReversed.value,
                            folder = folderName
                        )
                    )
                }
            }
        }
    }

    override fun savePosition(position: Int) { lastPosition = position }

    override fun onMediaLongClick(media: Media) {
        if (selectedMediaIds.value.isNotEmpty()) return
        componentScope.launch {
            selectedMediaIds.value = setOf(media.id)
            sideEffect.emit(MediaListSideEffect.Vibrate)
        }
    }

    override fun onDismissSelectMedia() {
        selectedMediaIds.value = emptySet()
        selectedMediaDates.value = emptySet()
    }

    override fun selectModeOnClickShare() {
        componentScope.launch {
            val mediaList = selectedMediaIds.value.mapNotNull {
                allMediaList.find { media -> media.id == it }
            }
            selectedMediaIds.value = emptySet()
            sideEffect.emit(MediaListSideEffect.ShareMedia(mediaList))
        }
    }

    override fun selectModeOnClickTrash() {
        componentScope.launch {
            val mediaList = selectedMediaIds.value.mapNotNull {
                allMediaList.find { media -> media.id == it }?.uri
            }
            mediaToTrashUris.value = mediaList.toSet()
            selectedMediaIds.value = emptySet()
        }
    }

    override fun onSelectDateClick(mediaDate: MediaDate) {
        selectedMediaIds.update { value ->
            value.toMutableSet().apply {
                val newSelectedMediaIds = allMediaList.filter { it.date == mediaDate }
                    .map(Media::id).toSet()
                val wasAlreadySelected = mediaDate in selectedMediaDates.value
                if (wasAlreadySelected) {
                    removeAll(newSelectedMediaIds)
                } else {
                    addAll(newSelectedMediaIds)
                }
            }
        }
        selectedMediaDates.update { value ->
            value.toMutableSet().apply {
                if (contains(mediaDate)) remove(mediaDate) else add(mediaDate)
            }
        }
    }

    override fun onDeleteMediaClick() {
        componentScope.launch {
            val uris = mediaToTrashUris.value
            onDismissTrashMedia()
            sideEffect.emit(MediaListSideEffect.DeleteMedia(uris))
        }
    }

    override fun onDismissTrashMedia() {
        mediaToTrashUris.value = emptySet()
        selectedMediaIds.value = emptySet()
    }

    private fun updateMedia(data: Map<MediaDate, List<Media>>) {
        media.value = data to data.mapValues { it.value.reversed() }.toSortedMap()
    }

    private fun editSelectedMedia(id: Long) {
        componentScope.safeLaunch(Dispatchers.Default) {
            sideEffect.emit(MediaListSideEffect.Vibrate)
            val value = selectedMediaIds.value.toMutableList().apply {
                if (contains(id)) remove(id) else add(id)
            }.toSet()
            selectedMediaIds.value = value
            checkAllMediaOfDaySelected(id)
        }
    }
    private fun checkAllMediaOfDaySelected(id: Long) {
        componentScope.safeLaunch(Dispatchers.Default) {
            val dateToCheck = allMediaList.find { it.id == id }?.date ?: return@safeLaunch
            val isAllMediaSelected = allMediaList.filter { it.date == dateToCheck }.map { it.id }
                .all { it in selectedMediaIds.value }
            selectedMediaDates.update { value ->
                value.toMutableSet().apply {
                    if (isAllMediaSelected) add(dateToCheck) else remove(dateToCheck)
                }
            }
        }
    }
}
