package ru.kvf.favorite.ui.list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.LikesRepository
import ru.kvf.core.domain.Photo
import ru.kvf.core.ui.VM
import ru.kvf.core.widgets.PHOTO_ITEM_LIKE_DURATION
import ru.kvf.favorite.domain.GetLikedPhotosUseCase

class FavoriteListViewModel(
    getLikedPhotosUseCase: GetLikedPhotosUseCase,
    private val likesRepository: LikesRepository
) : VM<FavoriteListState, FavoriteListSideEffect>(FavoriteListState()) {

    init {
        getLikedPhotosUseCase.get()
            .onEach { photos ->
                photosDataUpdated(photos)
            }.launchIn(viewModelScope)
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
        delay(PHOTO_ITEM_LIKE_DURATION)
        likesRepository.addToLikedList(id)
    }

    fun onReverseIconClick() = intent {
        reduce {
            state.copy(
                photos = state.photos.reversed(),
            )
        }
        postSideEffect(FavoriteListSideEffect.ScrollUp)
    }
}
