package ru.kvf.media.ui.list

import com.arkivanov.decompose.ComponentContext
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.componentCoroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.media.domain.GetFolderMediaUseCase
import ru.kvf.media.domain.GetSortedMediaUseCase

class RealMediaListComponent(
    componentContext: ComponentContext,
    private val onOutput: (MediaListComponent.Output) -> Unit,
    private val folder: String? = null,
    getSortedMediaUseCase: GetSortedMediaUseCase,
    private val getFolderMediaUseCase: GetFolderMediaUseCase,
    getLikedIdsListUseCase: GetLikedIdsListUseCase,
    private val getMediaUseCase: GetMediaUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase
) : ComponentContext by componentContext, MediaListComponent {

    private companion object {
        const val BSH_ANIM_DELAY = 1000L
    }

    private val scope = componentCoroutineScope()

    override val state = MutableStateFlow(MediaListState())

    override val sideEffect = MutableSharedFlow<MediaListSideEffect>()

    init {
        scope.collectFlow(getLikedIdsListUseCase()) { list ->
            state.update { state.value.copy(likedMedia = list.toImmutableList()) }
        }

        scope.collectFlow(
            gridCellsCountChangeUseCase.get(GridCellsCountChangeUseCase.Screen.MediaList)
        ) { count ->
            state.update { state.value.copy(gridCellsCount = count) }
        }

        if (folder != null) {
            scope.collectFlow(getFolderMediaUseCase.sorted(folder)) { media ->
                updateMedia(media)
            }
            state.update { state.value.copy(folderName = folder) }
        } else {
            scope.collectFlow(getSortedMediaUseCase()) { updateMedia(it) }
        }
    }

    override fun onGridCountClick() {
        scope.safeLaunch {
            val value = if (state.value.gridCellsCount == 4) 1 else state.value.gridCellsCount + 1
            gridCellsCountChangeUseCase.set(
                value = value,
                screen = GridCellsCountChangeUseCase.Screen.MediaList
            )
        }
    }

    override fun onLikeClick(id: Long) {
        scope.safeLaunch { handleLikeClickUseCase(id) }
    }

    override fun onReverseClick() {
        state.update { state.value.copy(reversed = state.value.reversed.not()) }
        scope.launch { sideEffect.emit(MediaListSideEffect.ScrollUp) }
    }

    override fun onMediaClick(mediaId: Long) {
        scope.safeLaunch {
            if (state.value.selectMediaModeEnable) {
                addSelectedMedia(mediaId)
            } else {
                if (folder != null) {
                    getFolderMediaUseCase(folder).firstOrNull()
                } else {
                    getMediaUseCase().firstOrNull()
                }?.let { list ->
                    val index = list.indexOfFirst { it.id == mediaId }
                    onOutput(
                        MediaListComponent.Output.OpenMediaRequested(
                            index = index,
                            reversed = state.value.reversed,
                            folder = folder
                        )
                    )
                }
            }
        }
    }

    override fun savePosition(position: Int) {
        state.update { state.value.copy(lastPosition = position) }
    }

    override fun onMediaLongClick(media: Media) {
        state.update {
            state.value.copy(selectedMediaIds = persistentSetOf(media.id))
        }
    }

    override fun onDismissSelectMedia() {
        state.update {
            state.value.copy(selectedMediaIds = persistentSetOf())
        }
    }

    override fun selectModeOnClickShare() {
        scope.launch {
            val allMedia = state.value.media.values.flatten()
            val mediaList = state.value.selectedMediaIds.mapNotNull {
                allMedia.find { media -> media.id == it }
            }
            state.update {
                state.value.copy(selectedMediaIds = persistentSetOf())
            }
            sideEffect.emit(MediaListSideEffect.ShareMedia(mediaList))
        }
    }

    override fun selectModeOnClickTrash() {
        scope.launch {
            val allMedia = state.value.media.values.flatten()
            val mediaList = state.value.selectedMediaIds.mapNotNull {
                allMedia.find { media -> media.id == it }?.uri
            }.toImmutableList()
            state.update {
                state.value.copy(
                    mediaToTrashUris = mediaList,
                    selectedMediaIds = persistentSetOf()
                )
            }
        }
    }

    override fun onDeleteMediaClick() {
        scope.launch {
            val uris = state.value.mediaToTrashUris
            onDismissTrashMedia()
            sideEffect.emit(MediaListSideEffect.DeleteMedia(uris))
        }
    }

    override fun onDismissTrashMedia() {
        state.update {
            state.value.copy(
                mediaToTrashUris = persistentListOf(),
                selectedMediaIds = persistentSetOf()
            )
        }
    }

    private fun updateMedia(media: Map<MediaDate, List<Media>>) {
        state.update {
            state.value.copy(
                media = media.toImmutableMap(),
                reversedMedia = media.mapValues { it.value.reversed() }.toSortedMap().toImmutableMap()
            )
        }
    }

    private fun addSelectedMedia(id: Long) {
        val newSet = state.value.selectedMediaIds.toMutableList().apply {
            add(id)
        }
        state.update { state.value.copy(selectedMediaIds = newSet.toImmutableSet()) }
    }
}
