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
import ru.kvf.core.domain.usecase.GetLikedMediaUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.mediabsh.MediaBSHComponent
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.notNegative
import ru.kvf.core.utils.safeLaunch

class RealFavoriteMediaComponent(
    componentContext: ComponentContext,
    getLikedMediaUseCase: GetLikedMediaUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase,
    componentFactory: ComponentFactory
) : ComponentContext by componentContext, FavoriteMediaComponent {

    private val componentScope = lifecycle.coroutineScope()

    override val media: StateFlow<List<Media>> = getLikedMediaUseCase()
        .stateIn(componentScope, SharingStarted.WhileSubscribed(5000), emptyList())

    override val mediaBSHComponent: MediaBSHComponent = componentFactory.createMediaBSHComponent(
        componentContext = childContext("favoriteMediaBSH"),
        media = media,
        output = ::mediaBSHOutput
    )

    override val selectedMediaIndex = MutableStateFlow(0)
    override val isReversed = MutableStateFlow(false)
    override val showDetailsBSH = MutableStateFlow(false)

    override fun onReverseClick() {
        isReversed.update { !it }
    }

    override fun onLikeClick(id: Long) {
        componentScope.safeLaunch { handleLikeClickUseCase(id) }
    }

    override fun onMediaClick(mediaId: Long) {
        val index = media.value.indexOfFirst { it.id == mediaId }.takeIf { it.notNegative() } ?: return
        mediaBSHComponent.setup(index)
        showDetailsBSH.update { true }
    }

    private fun mediaBSHOutput(output: MediaBSHComponent.Output) {
        when (output) {
            MediaBSHComponent.Output.DismissRequested -> showDetailsBSH.update { false }
        }
    }
}
