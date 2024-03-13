package ru.kvf.favorite.ui

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kvf.core.domain.usecase.GetLikedMediaUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.componentCoroutineScope
import ru.kvf.core.utils.safeLaunch

class RealFavoriteListComponent(
    componentContext: ComponentContext,
    private val onOutput: (FavoriteListComponent.Output) -> Unit,
    getLikedMediaUseCase: GetLikedMediaUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase,
) : ComponentContext by componentContext, FavoriteListComponent {

    private val scope = componentCoroutineScope()
    override val state = MutableStateFlow(FavoriteListState())
    override val sideEffect = MutableSharedFlow<FavoriteListSideEffect>()

    init {
        scope.collectFlow(getLikedMediaUseCase()) { media ->
            state.update {
                state.value.copy(
                    media = media,
                    noMediaFound = media.isEmpty()
                )
            }
        }
    }

    override fun onLikeClick(id: Long) {
        scope.safeLaunch { handleLikeClickUseCase(id) }
    }

    override fun onMediaClick(mediaId: Long) {
        val index = state.value.media.indexOfFirst { it.id == mediaId }
        onOutput(FavoriteListComponent.Output.OpenMediaRequested(index, state.value.reversed))
    }

    override fun onReverseClick() {
        state.update { state.value.copy(reversed = state.value.reversed.not()) }
        scope.launch { sideEffect.emit(FavoriteListSideEffect.ScrollUp) }
    }
}
