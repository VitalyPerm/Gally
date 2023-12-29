package ru.kvf.photos.ui.list

import kotlinx.coroutines.flow.firstOrNull
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate
import ru.kvf.core.domain.usecase.GetAllPhotosUseCase
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.ui.VM
import ru.kvf.photos.domain.GetSortedPhotosUseCase

class PhotosListViewModel(
    getSortedPhotosUseCase: GetSortedPhotosUseCase,
    getLikedIdsListUseCase: GetLikedIdsListUseCase,
    private val getAllPhotosUseCase: GetAllPhotosUseCase,
    private val gridCellsCountChangeUseCase: GridCellsCountChangeUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase
) : VM<PhotosListState, PhotosListSideEffect>(PhotosListState()) {

    init {
        collectFlow(getLikedIdsListUseCase()) { list ->
            intent { reduce { state.copy(likedPhotos = list) } }
        }

        collectFlow(gridCellsCountChangeUseCase.get(GridCellsCountChangeUseCase.Screen.PhotosList)) {
            intent { reduce { state.copy(gridCellsCount = it) } }
        }

        collectFlow(getSortedPhotosUseCase()) { updatePhotos(it) }
    }

    private fun updatePhotos(photos: Map<PhotoDate, List<Photo>>) = intent {
        reduce {
            state.copy(
                photos = photos,
                reversedPhotos = photos.mapValues { it.value.reversed() }.toSortedMap()
            )
        }
    }

    fun onGridCountClick() = intent {
        val value = if (state.gridCellsCount == 4) 1 else state.gridCellsCount + 1
        gridCellsCountChangeUseCase.set(
            value = value,
            screen = GridCellsCountChangeUseCase.Screen.PhotosList
        )
    }

    fun onLikeClick(id: Long) = intent { handleLikeClickUseCase(id) }

    fun onReverseClick() = intent {
        reduce { state.copy(reversed = state.reversed.not()) }
        postSideEffect(PhotosListSideEffect.ScrollUp)
    }

    fun calculatePhotoIndex(photoId: Long) = intent {
        getAllPhotosUseCase().firstOrNull()?.let { list ->
            val index = list.indexOfFirst { it.id == photoId }
            if (index != -1) {
                postSideEffect(PhotosListSideEffect.NavigateToDetails(index))
            }
        }
    }
}
