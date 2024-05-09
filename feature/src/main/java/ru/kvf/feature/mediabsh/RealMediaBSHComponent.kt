package ru.kvf.feature.mediabsh

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaIdsUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFavoriteClickUseCase
import ru.kvf.core.utils.MediaList
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.feature.trash.TrashComponent
import java.text.SimpleDateFormat
import java.util.Locale

class RealMediaBSHComponent(
    componentContext: ComponentContext,
    override val isTrash: Boolean,
    override val media: StateFlow<MediaList>,
    getFavoriteMediaIdsUseCase: GetFavoriteMediaIdsUseCase,
    private val handleFavoriteClickUseCase: HandleFavoriteClickUseCase
) : ComponentContext by componentContext, MediaBSHComponent {

    private companion object {
        const val TITLE_TIME_FORMAT = "dd MMMM yyyy HH:mm"
    }

    private val componentScope = coroutineScope()
    override val currentMediaIndex = MutableStateFlow(0)

    override val title: StateFlow<String> = combine(media, currentMediaIndex) { all, page ->
        all.data.getOrNull(page)?.timeStamp?.let { titleTimeFormat.format(it) } ?: ""
    }.stateIn(componentScope, SharingStarted.WhileSubscribed(5000), "")

    override val deleteDay: StateFlow<String?> = combine(media, currentMediaIndex) { all, page ->
        all.data.getOrNull(page)?.expiresTimeStamp?.let { deleteDaySdf.format(it.times(1000)) }
    }.stateIn(componentScope, SharingStarted.WhileSubscribed(5000), null)

    override val sideEffect = MutableSharedFlow<MediaBSHComponent.SideEffect>()
    override val optionsVisible = MutableStateFlow(true)
    override val visible = MutableStateFlow(false)
    private val titleTimeFormat = SimpleDateFormat(TITLE_TIME_FORMAT, Locale.getDefault())
    private val deleteDaySdf =
        SimpleDateFormat(TrashComponent.DELETE_DAY_FORMAT, Locale.getDefault())
    override val isFavorite: StateFlow<Boolean> = combine(
        media,
        currentMediaIndex,
        getFavoriteMediaIdsUseCase()
    ) { all, page, favoriteIds ->
        all.data.getOrNull(page)?.id in favoriteIds.data
    }.stateIn(componentScope, SharingStarted.WhileSubscribed(5000), false)

    override fun onShareClick() {
        componentScope.safeLaunch {
            sideEffect.emit(MediaBSHComponent.SideEffect.ShareMedia(getCurrentMedia()))
        }
    }

    override fun onTap() {
        optionsVisible.update { it.not() }
    }

    override fun onTrashClick() {
        componentScope.safeLaunch {
            sideEffect.emit(MediaBSHComponent.SideEffect.TrashMedia(getCurrentMedia().uri))
        }
    }

    override fun onUnTrashClick() {
    }

    override fun onFavoriteClick() {
        componentScope.safeLaunch { handleFavoriteClickUseCase(getCurrentMedia().id) }
    }

    override fun onDismissRequest() {
        visible.update { false }
    }

    override fun onPageChanged(page: Int) {
        currentMediaIndex.update { page }
    }

    override fun setup(startIndex: Int) {
        componentScope.launch {
            currentMediaIndex.update { startIndex }
            visible.update { true }
            sideEffect.emit(MediaBSHComponent.SideEffect.SetIndex(startIndex))
        }
    }

    override fun trashedSuccess() {
        // todo подумать что делать после удаления (MessageComponent)
    }

    private fun getCurrentMedia() = media.value.data[currentMediaIndex.value]
}
