package ru.kvf.favorite.ui

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.usecase.GetLikedMediaUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch

class RealFavoriteListComponent(
    componentContext: ComponentContext,
    private val onOutput: (FavoriteListComponent.Output) -> Unit,
    getLikedMediaUseCase: GetLikedMediaUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase,
) : ComponentContext by componentContext, FavoriteListComponent {

    private val componentScope = lifecycle.coroutineScope()

    override val media = MutableStateFlow(emptyList<Media>())
    override val isReversed = MutableStateFlow(false)
    override val sideEffect = MutableSharedFlow<FavoriteListSideEffect>()

    init {
        componentScope.collectFlow(getLikedMediaUseCase()) { data -> media.value = data }
    }

    override fun onLikeClick(id: Long) {
        componentScope.safeLaunch { handleLikeClickUseCase(id) }
    }

    override fun onMediaClick(mediaId: Long) {
        val index = media.value.indexOfFirst { it.id == mediaId }
        onOutput(FavoriteListComponent.Output.OpenMediaRequested(index, isReversed.value))
    }

    override fun onReverseClick() {
        componentScope.launch {
            isReversed.update { it.not() }
            sideEffect.emit(FavoriteListSideEffect.ScrollUp)
        }
    }
}
