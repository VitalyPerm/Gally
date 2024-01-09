package ru.kvf.favorite.ui.list

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.ui.VM
import ru.kvf.favorite.domain.GetLikedPhotosUseCase

class FavoriteListViewModel(
    getLikedPhotosUseCase: GetLikedPhotosUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase,
) : VM<FavoriteListState, FavoriteListSideEffect>(FavoriteListState()) {

    init {
        collectFlow(getLikedPhotosUseCase()) { photosDataUpdated(it) }
    }

    private fun photosDataUpdated(photos: List<Photo>) = intent {
        reduce {
            state.copy(
                photos = photos,
                noPhotosFound = photos.isEmpty()
            )
        }
    }

    fun onLikeClick(id: Long) = intent {
        handleLikeClickUseCase(id)
    }

    fun onReverseIconClick() = intent {
        reduce {
            state.copy(
                photos = state.photos,
            )
        }
        postSideEffect(FavoriteListSideEffect.ScrollUp)
    }
}
