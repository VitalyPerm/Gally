package ru.kvf.photos.ui.list

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.ui.VM
import ru.kvf.core.utils.Log
import ru.kvf.photos.domain.GetSortedPhotosUseCase

class PhotosListViewModel(
    getSortedPhotosUseCase: GetSortedPhotosUseCase,
    getLikedIdsListUseCase: GetLikedIdsListUseCase,
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
                reversedPhotos = photos.toSortedMap(Comparator.reverseOrder())
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
}
