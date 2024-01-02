package ru.kvf.photos.details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.PhotosRepository
import ru.kvf.core.ui.VM
import ru.kvf.core.utils.log

class DetailsViewModel(
    photosRepository: PhotosRepository,
) : VM<DetailsState, DetailsSideEffect>(DetailsState()) {

    init {
        photosRepository.photos
            .onEach { photos ->
                intent {
                    reduce {
                        state.copy(photos = photos)
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun start(photoId: Long) = intent {
        val index = state.photos.indexOfFirst { it.id == photoId }.takeIf { it != -1 } ?: 0
        log("index = $index")
        reduce {
            state.copy(
                startIndex = index,
                loading = false
            )
        }
    }
}
