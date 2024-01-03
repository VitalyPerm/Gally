package ru.kvf.favorite.ui.details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.core.ui.VM
import ru.kvf.core.utils.log

class FavoriteDetailsViewModel(
    photosRepository: PhotosRepository,
) : VM<PhotoDetailsState, DetailsSideEffect>(PhotoDetailsState()) {

    init {
        photosRepository.photosFlow
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
        reduce {
            state.copy(
                startIndex = index,
                loading = false
            )
        }
    }
}
