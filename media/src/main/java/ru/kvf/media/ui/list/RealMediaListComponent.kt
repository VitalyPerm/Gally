package ru.kvf.media.ui.list

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
import ru.kvf.core.createMediaBSHComponent
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.mediabsh.MediaBSHComponent
import ru.kvf.core.utils.LongSet
import ru.kvf.core.utils.MediaDateSet
import ru.kvf.core.utils.MediaMap
import ru.kvf.core.utils.UriSet
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.media.domain.GetFolderMediaUseCase
import ru.kvf.media.domain.GetSortedMediaUseCase

class RealMediaListComponent(
    componentContext: ComponentContext,
    override val folderName: String? = null,
    getSortedMediaUseCase: GetSortedMediaUseCase,
    getFolderMediaUseCase: GetFolderMediaUseCase,
    getLikedIdsListUseCase: GetLikedIdsListUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase,
    componentFactory: ComponentFactory,
    private val context: Context
) : ComponentContext by componentContext, MediaListComponent {

    private val componentScope = coroutineScope()

    override val gridCellsCount = gridCellsCountChangeUseCase
        .get(GridCellsCountChangeUseCase.Screen.MediaList)
        .stateIn(componentScope, SharingStarted.Lazily, 1)

    override val mediaMap = MutableStateFlow(MediaMap.EMPTY to MediaMap.EMPTY)
    override val likedMedia: StateFlow<LongSet> = getLikedIdsListUseCase()
        .stateIn(componentScope, SharingStarted.Lazily, LongSet.EMPTY)
    override val sortReversed = MutableStateFlow(false)
    override val selectedMediaIds = MutableStateFlow(LongSet.EMPTY)
    override val mediaToTrashUris = MutableStateFlow(UriSet.EMPTY)
    override val selectedMediaDates = MutableStateFlow(MediaDateSet.EMPTY)
    override var lastPosition = 0
    override val sideEffect = MutableSharedFlow<MediaListComponent.SideEffect>()

    private val allMedia = MutableStateFlow<List<Media>>(emptyList())
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

    override fun onLikeClick(id: Long) {
        componentScope.safeLaunch { handleLikeClickUseCase(id) }
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
                val media = allMedia.value.find { it.id == mediaId } ?: return@safeLaunch
                context.imageLoader.execute(
                    ImageRequest.Builder(context)
                        .data(media.uri)
                        .size(Size.ORIGINAL)
                        .build()
                )
                val index = allMedia.value.indexOf(media)
                mediaBSHComponent.setup(index)
            }
        }
    }

    override fun savePosition(position: Int) { lastPosition = position }

    override fun onMediaLongClick(media: Media) {
        if (selectedMediaIds.value.data.isNotEmpty()) return
        componentScope.launch {
            selectedMediaIds.value = LongSet.from(setOf(media.id))
            sideEffect.emit(MediaListComponent.SideEffect.Vibrate)
        }
    }

    override fun onDismissSelectMedia() {
        selectedMediaIds.value = LongSet.EMPTY
        selectedMediaDates.value = MediaDateSet.EMPTY
    }

    override fun selectModeOnClickShare() {
        componentScope.launch {
            val mediaList = selectedMediaIds.value.data.mapNotNull {
                allMediaList.find { media -> media.id == it }
            }
            selectedMediaIds.value = LongSet.EMPTY
            sideEffect.emit(MediaListComponent.SideEffect.ShareMedia(mediaList))
        }
    }

    override fun selectModeOnClickTrash() {
        componentScope.launch {
            val mediaList = selectedMediaIds.value.data.mapNotNull {
                allMediaList.find { media -> media.id == it }?.uri
            }
            mediaToTrashUris.value = UriSet.from(mediaList.toSet())
            selectedMediaIds.value = LongSet.EMPTY
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

    override fun onDeleteMediaClick() {
        componentScope.launch {
            val uris = mediaToTrashUris.value
            onDismissTrashMedia()
            sideEffect.emit(MediaListComponent.SideEffect.TrashMedia(uris.data))
        }
    }

    override fun onDismissTrashMedia() {
        mediaToTrashUris.value = UriSet.EMPTY
        selectedMediaIds.value = LongSet.EMPTY
    }

    private fun updateMedia(data: Map<MediaDate, List<Media>>) {
        mediaMap.value = MediaMap.from(data) to MediaMap.from(data.mapValues { it.value.reversed() }.toSortedMap())
        allMedia.update { data.values.flatten() }
    }

    private fun editSelectedMedia(id: Long) {
        componentScope.safeLaunch(Dispatchers.Default) {
            sideEffect.emit(MediaListComponent.SideEffect.Vibrate)
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
