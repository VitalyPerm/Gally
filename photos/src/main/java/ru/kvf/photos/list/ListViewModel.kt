package ru.kvf.photos.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.kvf.core.data.CustomDate
import ru.kvf.core.domain.Folder
import ru.kvf.core.domain.Photo
import ru.kvf.core.domain.PhotosRepository

class ListViewModel(
    private val photosRepository: PhotosRepository
) : ViewModel(), ContainerHost<PhotosState, PhotosSideEffect> {

    override val container: Container<PhotosState, PhotosSideEffect> = container(PhotosState())
    private val normalPhotosMap = sortedMapOf<CustomDate, List<Photo>>(Comparator.reverseOrder())
    private val reversedPhotosMap = sortedMapOf<CustomDate, List<Photo>>()

    init {
        viewModelScope.launch {
            photosRepository.fetch()
        }
        photosRepository.data
            .onEach { (photos, folders) ->
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
    }
}
