package ru.kvf.media.ui.detail

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.usecase.GetLikedMediaUseCase
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RealMediaComponent(
    componentContext: ComponentContext,
    override val startIndex: Int,
    override val isReversed: Boolean,
    isFavoriteOnly: Boolean = false,
    folder: String? = null,
    getMediaUseCase: GetMediaUseCase,
    getLikedMediaUseCase: GetLikedMediaUseCase,
) : ComponentContext by componentContext, MediaComponent {

    private companion object {
        const val TITLE_HIDING_TIMEOUT = 5000L
        const val TITLE_TIME_FORMAT = "dd.MM.yyyy HH:mm"
    }

    private val componentScope = lifecycle.coroutineScope()

    override val media = MutableStateFlow(emptyList<Media>())
    override val optionsVisible = MutableStateFlow(false)
    override val sideEffect = MutableSharedFlow<MediaComponent.SideEffect>()

    private var titleHidingJob: Job? = null
    private val titleTimeFormat = SimpleDateFormat(TITLE_TIME_FORMAT, Locale.getDefault())

    private val currentPage = MutableStateFlow(startIndex)
    override val title: StateFlow<String> = combine(media, currentPage) { all, page ->
        titleTimeFormat.format(Date(all[page].timeStamp))
    }.stateIn(componentScope, SharingStarted.WhileSubscribed(5000), "")

    init {
        componentScope.safeLaunch {
            val mediaList = when {
                isFavoriteOnly -> getLikedMediaUseCase().firstOrNull()
                folder != null -> getMediaUseCase().firstOrNull()
                    ?.filter { it.folder == folder }
                else -> getMediaUseCase().firstOrNull()
            } ?: return@safeLaunch
            media.value = mediaList
        }
    }

    override fun onPageChanged(page: Int) {
        currentPage.value = page
    }

    override fun onSingleTap() {
        optionsVisible.update { it.not() }
        titleHidingTimer()
    }

    override fun onShareClick() {
        componentScope.safeLaunch {
            sideEffect.emit(MediaComponent.SideEffect.ShareMedia(getCurrentMedia()))
        }
    }

    override fun onTrashClick() {
        componentScope.safeLaunch {
            sideEffect.emit(MediaComponent.SideEffect.TrashMedia(getCurrentMedia().uri))
        }
    }

    override fun trashedSuccess() {
        media.update { it.toMutableList().apply { remove(getCurrentMedia()) } }
    }

    private fun getCurrentMedia() = media.value[currentPage.value]

    private fun titleHidingTimer() {
        if (optionsVisible.value.not()) return
        titleHidingJob?.cancel()
        titleHidingJob = componentScope.safeLaunch {
            delay(TITLE_HIDING_TIMEOUT)
            optionsVisible.value = false
        }
    }
}
