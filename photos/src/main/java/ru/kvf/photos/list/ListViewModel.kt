package ru.kvf.photos.list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.data.CustomDate
import ru.kvf.core.domain.Folder
import ru.kvf.core.domain.LikesRepository
import ru.kvf.core.domain.Photo
import ru.kvf.core.domain.PhotosRepository
import ru.kvf.core.ui.VM

class ListViewModel(
    private val photosRepository: PhotosRepository,
    private val likesRepository: LikesRepository
) : VM<PhotosState, PhotosSideEffect>(PhotosState()) {

    private val normalPhotosMap = sortedMapOf<CustomDate, List<Photo>>(Comparator.reverseOrder())
    private val reversedPhotosMap = sortedMapOf<CustomDate, List<Photo>>()

    init {
        likesRepository.getLikedListFlow()
            .onEach { likeList ->
                intent {
                    reduce {
                        state.copy(likedPhotos = likeList)
                    }
                }
            }.launchIn(viewModelScope)
        viewModelScope.launch {
            photosRepository.fetch()
        }
        combine(photosRepository.foldersFlow, photosRepository.photosSortedByDateFlow) { folders, photos ->
            photosDataUpdated(photos, folders)
        }.launchIn(viewModelScope)
    }

    private fun photosDataUpdated(photos: Map<CustomDate, List<Photo>>, folders: List<Folder>) = intent {
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
        likesRepository.addToLikedList(id)
    }

    fun reload() = intent {
        reduce { state.copy(loading = true) }
        photosRepository.fetch()
    }

    fun onReverseIconClick() = intent {
        reduce {
            state.copy(
                reversed = state.reversed.not(),
                photos = if (state.reversed.not()) reversedPhotosMap else normalPhotosMap,
            )
        }
        postSideEffect(PhotosSideEffect.ScrollUp)
    }
}
