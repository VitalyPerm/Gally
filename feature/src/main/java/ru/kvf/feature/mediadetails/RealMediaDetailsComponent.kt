package ru.kvf.feature.mediadetails

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.kvf.core.domain.entities.MimeType
import ru.kvf.core.domain.usecase.DeleteMediaUseCase
import ru.kvf.core.domain.usecase.ShareMediaUseCase
import ru.kvf.core.domain.usecase.TrashMediaUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaIdsUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFavoriteClickUseCase
import ru.kvf.core.utils.MediaList
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.feature.media.domain.GetSortedMediaUseCase
import ru.kvf.feature.trash.TrashComponent
import java.text.SimpleDateFormat
import java.util.Locale

class RealMediaDetailsComponent(
    componentContext: ComponentContext,
    override val type: MediaDetailsComponent.Type,
    private val initialMediaId: Long,
    getFavoriteMediaIdsUseCase: GetFavoriteMediaIdsUseCase,
    private val handleFavoriteClickUseCase: HandleFavoriteClickUseCase,
    private val shareMediaUseCase: ShareMediaUseCase,
    private val trashMediaUseCase: TrashMediaUseCase,
    private val deleteMediaUseCase: DeleteMediaUseCase,
    getFavoriteMediaUseCase: GetFavoriteMediaUseCase,
    getSortedMediaUseCase: GetSortedMediaUseCase
) : ComponentContext by componentContext, MediaDetailsComponent {

    private companion object {
        const val TITLE_TIME_FORMAT = "dd MMMM yyyy HH:mm"
    }

    private val componentScope = coroutineScope()

    override val media = when (type) {
        MediaDetailsComponent.Type.All -> getSortedMediaUseCase().map {
            MediaList.from(it.values.flatten())
        }

        MediaDetailsComponent.Type.Favorite -> getFavoriteMediaUseCase().map(MediaList::from)
    }.stateIn(componentScope, SharingStarted.Eagerly, MediaList.EMPTY)

    override val currentMediaIndex = MutableStateFlow<Int?>(null)

    private val currentMedia = combine(media, currentMediaIndex) { all, page ->
        page?.let(all.data::getOrNull)
    }

    override val isTrash: Boolean = false

    override val title: StateFlow<String?> = currentMedia.map { media ->
        media?.timeStamp?.takeIf { it > 0 }?.let { time -> titleTimeFormat.format(time) }
    }.stateIn(componentScope, SharingStarted.WhileSubscribed(5000), null)

    override val deleteDay: StateFlow<String?> = currentMedia.map {
        it?.expiresTimeStamp?.let { time -> deleteDaySdf.format(time.times(1000)) }
    }.stateIn(componentScope, SharingStarted.WhileSubscribed(5000), null)

    override val optionsVisible = MutableStateFlow(true)
    private val titleTimeFormat = SimpleDateFormat(TITLE_TIME_FORMAT, Locale.getDefault())
    private val deleteDaySdf =
        SimpleDateFormat(TrashComponent.DELETE_DAY_FORMAT, Locale.getDefault())
    override val isFavorite: StateFlow<Boolean> = combine(
        media,
        currentMediaIndex,
        getFavoriteMediaIdsUseCase()
    ) { all, page, favoriteIds ->
        page?.let(all.data::getOrNull)?.let { it.id in favoriteIds.data } ?: false
    }.stateIn(componentScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        currentMedia.onEach { if (it?.mimeType == MimeType.Video) optionsVisible.update { false } }
            .launchIn(componentScope)

        componentScope.safeLaunch {
            media.firstOrNull { it.data.isNotEmpty() }?.data
                ?.indexOfFirst { it.id == initialMediaId }?.let { index ->
                    currentMediaIndex.update { index }
                    Log.d("check___", "set $index")
                }
        }
    }

    override fun onShareClick() {
        componentScope.safeLaunch {
            getCurrentMedia()?.let { shareMediaUseCase(listOf(it)) }
        }
    }

    override fun onTap() {
        optionsVisible.update { it.not() }
    }

    override fun onTrashClick() {
        componentScope.safeLaunch {
            getCurrentMedia()?.uri?.let { trashMediaUseCase(setOf(it), trash = true) }
        }
    }

    override fun onDeleteClick() {
        componentScope.safeLaunch {
            getCurrentMedia()?.uri?.let {
                deleteMediaUseCase(setOf(it))
            }
        }
    }

    override fun onUnTrashClick() {
        componentScope.safeLaunch {
            getCurrentMedia()?.uri?.let {
                trashMediaUseCase(setOf(it), trash = false)
            }
        }
    }

    override fun onFavoriteClick() {
        getCurrentMedia()?.let {
            componentScope.safeLaunch { handleFavoriteClickUseCase(it.id) }
        }
    }

    override fun onPageChanged(page: Int) {
        currentMediaIndex.update { page }
    }

    override fun trashedSuccess() {
        // todo подумать что делать после удаления (MessageComponent)
    }

    private fun getCurrentMedia() = currentMediaIndex.value?.let { media.value.data[it] }
}
