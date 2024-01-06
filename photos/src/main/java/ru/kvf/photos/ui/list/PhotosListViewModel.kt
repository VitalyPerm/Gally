package ru.kvf.photos.ui.list

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.ui.VM
import ru.kvf.photos.domain.GetFoldersUseCase
import ru.kvf.photos.domain.GetSortedPhotosUseCase

class PhotosListViewModel(
    getSortedPhotosUseCase: GetSortedPhotosUseCase,
    getFoldersUseCase: GetFoldersUseCase,
    getLikedIdsListUseCase: GetLikedIdsListUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase
) : VM<PhotosListState, PhotosListSideEffect>(PhotosListState()) {

    init {
        collectFlow(getLikedIdsListUseCase()) { list ->
            intent { reduce { state.copy(likedPhotos = list) } }
        }

        collectFlow(getFoldersUseCase()) { updateFolders(it) }

        collectFlow(getSortedPhotosUseCase()) { updatePhotos(it) }
    }

    private fun updateFolders(folders: List<Folder>) = intent {
        reduce { state.copy(folders = folders) }
    }

    private fun updatePhotos(photos: Map<PhotoDate, List<Photo>>) = intent {
        reduce {
            state.copy(
                normalPhotos = photos,
                reversedPhotos = photos.toSortedMap(Comparator.reverseOrder())
            )
        }
    }

    private fun photosDataUpdated(photos: Map<PhotoDate, List<Photo>>, folders: List<Folder>) = intent {
        reduce {
            state.copy(
                normalPhotos = photos,
                reversedPhotos = photos.toSortedMap(Comparator.reverseOrder()),
                folders = folders,
                noPhotosFound = photos.isEmpty()
            )
        }
    }

    fun onChangeViewModeClick() = intent {
        with(state) {
            reduce {
                copy(showFolders = showFolders.not())
            }
        }
    }

    fun onLikeClick(id: Long) = intent { handleLikeClickUseCase(id) }

    fun onReverseIconClick() = intent {
        reduce { state.copy(reversed = state.reversed.not()) }
        postSideEffect(PhotosListSideEffect.ScrollUp)
    }
}
