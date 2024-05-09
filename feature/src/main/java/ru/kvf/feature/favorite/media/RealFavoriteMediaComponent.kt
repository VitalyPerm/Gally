package ru.kvf.feature.favorite.media

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.kvf.core.ComponentFactory
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFavoriteClickUseCase
import ru.kvf.core.utils.MediaList
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.notNegative
import ru.kvf.core.utils.safeLaunch
import ru.kvf.createMediaBSHComponent
import ru.kvf.feature.mediabsh.MediaBSHComponent

class RealFavoriteMediaComponent(
    componentContext: ComponentContext,
    getFavoriteMediaUseCase: GetFavoriteMediaUseCase,
    private val handleMediaFavoriteClickUseCase: HandleFavoriteClickUseCase,
    componentFactory: ComponentFactory
) : ComponentContext by componentContext, FavoriteMediaComponent {

    private val componentScope = coroutineScope()

    override val media: StateFlow<MediaList> = getFavoriteMediaUseCase().map(MediaList::from)
        .stateIn(componentScope, SharingStarted.WhileSubscribed(5000), MediaList.EMPTY)

    override val mediaBSHComponent: MediaBSHComponent = componentFactory.createMediaBSHComponent(
        componentContext = childContext("favoriteMediaBSH"),
        media = media
    )

    override val selectedMediaIndex = MutableStateFlow(0)
    override val showDetailsBSH = MutableStateFlow(false)

    override fun onMediaClick(mediaId: Long) {
        val index =
            media.value.data.indexOfFirst { it.id == mediaId }.takeIf { it.notNegative() } ?: return
        mediaBSHComponent.setup(index)
    }

    override fun onMediaLongClick(mediaId: Long) {
        componentScope.safeLaunch { handleMediaFavoriteClickUseCase(mediaId) }
    }
}
