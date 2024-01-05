package ru.kvf.favorite.ui.details

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.ui.VM
import ru.kvf.favorite.domain.GetLikedPhotosUseCase

class FavoriteDetailsViewModel(
    selectedPhotoId: Long,
    getLikedPhotosUseCase: GetLikedPhotosUseCase
) : VM<PhotoDetailsState, DetailsSideEffect>(PhotoDetailsState()) {

    init {
        collectFlow(getLikedPhotosUseCase()) { photos ->
            val index = photos.indexOfFirst { it.id == selectedPhotoId }.takeIf { it != -1 } ?: 0
            intent {
                reduce {
                    state.copy(photos = photos, startIndex = index)
                }
            }
        }
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
