package ru.kvf.gally.feature.photos.ui.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kvf.gally.App
import ru.kvf.gally.feature.photos.domain.Photo
import ru.kvf.gally.feature.photos.domain.PhotosRepository

class PhotosRootViewModel(
    private val photosRepository: PhotosRepository
) : ViewModel(), ContainerHost<PhotosRootState, PhotosRootSideEffect> {

    override val container: Container<PhotosRootState, PhotosRootSideEffect> = container(
        PhotosRootState()
    )

    private val photos = MutableStateFlow<List<Photo>>(emptyList())

    init {
        App.log("init")
        viewModelScope.launch {
            val photos = photosRepository.getPhotos()
            val folders = photosRepository.getFolders()
            App.log(photos.toString())
            App.log(folders.toString())
        }

        photos
            .onEach {
                App.log(it.toString())
            }.launchIn(viewModelScope)
    }
}
