package ru.kvf.feature.favorite.media

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFavoriteClickUseCase
import ru.kvf.core.utils.MediaList
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch

class RealFavoriteMediaComponent(
    componentContext: ComponentContext,
    private val onOutput: (FavoriteMediaComponent.Output) -> Unit,
    getFavoriteMediaUseCase: GetFavoriteMediaUseCase,
    private val handleMediaFavoriteClickUseCase: HandleFavoriteClickUseCase,
) : ComponentContext by componentContext, FavoriteMediaComponent {

    private val componentScope = coroutineScope()

    override val media: StateFlow<MediaList> = getFavoriteMediaUseCase().map(MediaList::from)
        .stateIn(componentScope, SharingStarted.WhileSubscribed(5000), MediaList.EMPTY)

    override val selectedMediaIndex = MutableStateFlow(0)
    override val showDetailsBSH = MutableStateFlow(false)

    override fun onMediaClick(mediaId: Long) {
        onOutput(FavoriteMediaComponent.Output.MediaDetailsRequested(mediaId))
    }

    override fun onMediaLongClick(mediaId: Long) {
        componentScope.safeLaunch { handleMediaFavoriteClickUseCase(mediaId) }
    }
}
