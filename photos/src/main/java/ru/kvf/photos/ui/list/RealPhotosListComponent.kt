package ru.kvf.photos.ui.list

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate
import ru.kvf.core.domain.usecase.GetAllPhotosUseCase
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.utils.Log
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.componentCoroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.photos.domain.GetFolderPhotosUseCase
import ru.kvf.photos.domain.GetSortedPhotosUseCase

class RealPhotosListComponent(
    componentContext: ComponentContext,
    private val onOutput: (PhotosListComponent.Output) -> Unit,
    private val folder: String? = null,
    getSortedPhotosUseCase: GetSortedPhotosUseCase,
    private val getFolderPhotosUseCase: GetFolderPhotosUseCase,
    getLikedIdsListUseCase: GetLikedIdsListUseCase,
    private val getAllPhotosUseCase: GetAllPhotosUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase
) : ComponentContext by componentContext, PhotosListComponent {

    private val scope = componentCoroutineScope()

    override val state = MutableStateFlow(PhotosListState())

    override val sideEffect = MutableSharedFlow<PhotosListSideEffect>()

    init {
        scope.collectFlow(getLikedIdsListUseCase()) { list ->
            state.update { state.value.copy(likedPhotos = list) }
        }

        scope.collectFlow(
            gridCellsCountChangeUseCase.get(GridCellsCountChangeUseCase.Screen.PhotosList)
        ) { count ->
            state.update { state.value.copy(gridCellsCount = count) }
        }

        if (folder != null) {
            scope.collectFlow(getFolderPhotosUseCase.sorted(folder)) { photos ->
                updatePhotos(photos)
            }
            state.update { state.value.copy(folderName = folder) }
        } else {
            scope.collectFlow(getSortedPhotosUseCase()) { updatePhotos(it) }
        }
    }

    override fun onGridCountClick() {
        scope.safeLaunch {
            val value = if (state.value.gridCellsCount == 4) 1 else state.value.gridCellsCount + 1
            gridCellsCountChangeUseCase.set(
                value = value,
                screen = GridCellsCountChangeUseCase.Screen.PhotosList
            )
        }
    }

    override fun onLikeClick(id: Long) {
        scope.safeLaunch { handleLikeClickUseCase(id) }
    }

    override fun onReverseClick() {
        state.update { state.value.copy(reversed = state.value.reversed.not()) }
        scope.launch { sideEffect.emit(PhotosListSideEffect.ScrollUp) }
    }

    override fun onPhotoClick(photoId: Long) {
        scope.safeLaunch {
            if (folder != null) {
                getFolderPhotosUseCase(folder).firstOrNull()
            } else {
                getAllPhotosUseCase().firstOrNull()
            }?.let { list ->
                val index = list.indexOfFirst { it.id == photoId }
                onOutput(
                    PhotosListComponent.Output.OpenPhotoRequested(
                        index = index,
                        reversed = state.value.reversed,
                        folder = folder
                    )
                )
            }
        }
    }

    override fun savePosition(position: Int) {
        state.update { state.value.copy(lastPosition = position) }
    }

    private fun updatePhotos(photos: Map<PhotoDate, List<Photo>>) {
        state.update {
            state.value.copy(
                photos = photos,
                reversedPhotos = photos.mapValues { it.value.reversed() }.toSortedMap()
            )
        }
    }
}
