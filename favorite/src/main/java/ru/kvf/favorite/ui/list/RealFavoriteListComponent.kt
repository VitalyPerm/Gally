package ru.kvf.favorite.ui.list

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.componentCoroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.favorite.domain.GetLikedPhotosUseCase

class RealFavoriteListComponent(
    componentContext: ComponentContext,
    getLikedPhotosUseCase: GetLikedPhotosUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase,
) : ComponentContext by componentContext, FavoriteListComponent {

    private val scope = componentCoroutineScope()

    override val state = MutableStateFlow(FavoriteListState())
    override val sideEffect = MutableSharedFlow<FavoriteListSideEffect>()

    init {
        scope.collectFlow(getLikedPhotosUseCase()) { photos ->
            state.update {
                state.value.copy(
                    photos = photos,
                    noPhotosFound = photos.isEmpty()
                )
            }
        }
    }

    override fun onLikeClick(id: Long) {
        scope.safeLaunch { handleLikeClickUseCase(id) }
    }

    override fun onReverseIconClick() {
        state.update { state.value.copy(photos = state.value.photos) }
        scope.launch { sideEffect.emit(FavoriteListSideEffect.ScrollUp) }
    }
}
