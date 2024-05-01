package ru.kvf.favorite.ui.media

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.kvf.core.ComponentFactory
import ru.kvf.core.createMediaBSHComponent
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaUseCase
import ru.kvf.core.domain.usecase.favorite.HandleMediaDoubleClickUseCase
import ru.kvf.core.mediabsh.MediaBSHComponent
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.notNegative
import ru.kvf.core.utils.safeLaunch

class RealFavoriteMediaComponent(
    componentContext: ComponentContext,
    getFavoriteMediaUseCase: GetFavoriteMediaUseCase,
    private val handleMediaDoubleClickUseCase: HandleMediaDoubleClickUseCase,
    componentFactory: ComponentFactory
) : ComponentContext by componentContext, FavoriteMediaComponent {

    private val componentScope = coroutineScope()

    override val media: StateFlow<List<Media>> = getFavoriteMediaUseCase()
        .stateIn(componentScope, SharingStarted.WhileSubscribed(5000), emptyList())

    override val mediaBSHComponent: MediaBSHComponent = componentFactory.createMediaBSHComponent(
        componentContext = childContext("favoriteMediaBSH"),
        media = media
    )

    override val selectedMediaIndex = MutableStateFlow(0)
    override val isReversed = MutableStateFlow(false)
    override val showDetailsBSH = MutableStateFlow(false)

    override fun onReverseClick() {
        isReversed.update { !it }
    }

    override fun onLikeClick(id: Long) {
        componentScope.safeLaunch { handleMediaDoubleClickUseCase(id) }
    }

    override fun onMediaClick(mediaId: Long) {
        val index = media.value.indexOfFirst { it.id == mediaId }.takeIf { it.notNegative() } ?: return
        mediaBSHComponent.setup(index)
    }
}
