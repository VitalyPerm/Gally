package ru.kvf.feature.mediabsh

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kvf.core.domain.entities.MimeType
import ru.kvf.core.domain.usecase.DeleteMediaUseCase
import ru.kvf.core.domain.usecase.ShareMediaUseCase
import ru.kvf.core.domain.usecase.TrashMediaUseCase
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
    private val handleFavoriteClickUseCase: HandleFavoriteClickUseCase,
    private val shareMediaUseCase: ShareMediaUseCase,
    private val trashMediaUseCase: TrashMediaUseCase,
    private val deleteMediaUseCase: DeleteMediaUseCase
) : ComponentContext by componentContext, MediaBSHComponent {

    private companion object {
        const val TITLE_TIME_FORMAT = "dd MMMM yyyy HH:mm"
    }

    private val componentScope = coroutineScope()
    override val currentMediaIndex = MutableStateFlow(0)

    private val currentMedia = combine(media, currentMediaIndex) { all, page ->
        all.data.getOrNull(page)
    }

    override val title: StateFlow<String?> = currentMedia.map { media ->
        media?.timeStamp?.takeIf { it > 0 }?.let { time -> titleTimeFormat.format(time) }
    }.stateIn(componentScope, SharingStarted.WhileSubscribed(5000), null)

    override val deleteDay: StateFlow<String?> = currentMedia.map {
        it?.expiresTimeStamp?.let { time -> deleteDaySdf.format(time.times(1000)) }
    }.stateIn(componentScope, SharingStarted.WhileSubscribed(5000), null)

    override val optionsVisible = MutableStateFlow(true)
    override val visible = MutableStateFlow(false)
    override val setIndex = MutableSharedFlow<Int>()
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

    init {
        currentMedia.onEach { if (it?.mimeType == MimeType.Video) optionsVisible.update { false } }
            .launchIn(componentScope)
    }

    override fun onShareClick() {
        componentScope.safeLaunch {
            shareMediaUseCase(listOf(getCurrentMedia()))
        }
    }

    override fun onTap() {
        optionsVisible.update { it.not() }
    }

    override fun onTrashClick() {
        componentScope.safeLaunch { trashMediaUseCase(setOf(getCurrentMedia().uri), trash = true) }
    }

    override fun onDeleteClick() {
        componentScope.safeLaunch {
            deleteMediaUseCase(setOf(getCurrentMedia().uri))
        }
    }

    override fun onUnTrashClick() {
        componentScope.safeLaunch { trashMediaUseCase(setOf(getCurrentMedia().uri), trash = false) }
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
            setIndex.emit(startIndex)
        }
    }

    override fun trashedSuccess() {
        // todo подумать что делать после удаления (MessageComponent)
    }

    private fun getCurrentMedia() = media.value.data[currentMediaIndex.value]
}
