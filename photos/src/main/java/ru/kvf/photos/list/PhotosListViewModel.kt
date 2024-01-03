package ru.kvf.photos.list

import kotlinx.coroutines.delay
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.domain.usecase.GetSortedPhotosAndFoldersUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.domain.usecase.LoadPhotosUseCase
import ru.kvf.core.ui.VM

class PhotosListViewModel(
    private val loadPhotosUseCase: LoadPhotosUseCase,
    getSortedPhotosAndFoldersUseCase: GetSortedPhotosAndFoldersUseCase,
    getLikedIdsListUseCase: GetLikedIdsListUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase
) : VM<PhotosListState, PhotosListSideEffect>(PhotosListState()) {

    private val normalPhotosMap = sortedMapOf<PhotoDate, List<Photo>>(Comparator.reverseOrder())
    private val reversedPhotosMap = sortedMapOf<PhotoDate, List<Photo>>()

    init {
        collectFlow(getLikedIdsListUseCase()) { list ->
            intent { reduce { state.copy(likedPhotos = list) } }
        }

        safeLaunch {
            loadPhotosUseCase()
        }

        collectFlow(getSortedPhotosAndFoldersUseCase()) { (folders, photos) ->
            photosDataUpdated(photos, folders)
        }
    }

    private fun photosDataUpdated(photos: Map<PhotoDate, List<Photo>>, folders: List<Folder>) = intent {
        reduce {
            normalPhotosMap.putAll(photos)
            reversedPhotosMap.putAll(photos)
            state.copy(
                loading = false,
                photos = if (state.reversed) reversedPhotosMap else normalPhotosMap,
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

    fun onLikeClick(id: Long) = intent {
        delay(1000)
        handleLikeClickUseCase(id)
    }

    fun onReverseIconClick() = intent {
        reduce {
            state.copy(
                reversed = state.reversed.not(),
                photos = if (state.reversed.not()) reversedPhotosMap else normalPhotosMap,
            )
        }
        postSideEffect(PhotosListSideEffect.ScrollUp)
    }
}
