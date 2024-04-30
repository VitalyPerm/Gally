package ru.kvf.core.mediabsh

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import java.text.SimpleDateFormat
import java.util.Locale

class RealMediaBSHComponent(
    componentContext: ComponentContext,
    override val media: StateFlow<List<Media>>,
) : ComponentContext by componentContext, MediaBSHComponent {

    private companion object {
        const val TITLE_HIDING_TIMEOUT = 5000L
        const val TITLE_TIME_FORMAT = "dd.MM.yyyy HH:mm"
    }

    private val componentScope = lifecycle.coroutineScope()
    private val currentIndex = MutableStateFlow(0)
    private var titleHidingJob: Job? = null
    private val titleTimeFormat = SimpleDateFormat(TITLE_TIME_FORMAT, Locale.getDefault())

    override val title: StateFlow<String> = combine(media, currentIndex) { all, page ->
        all.getOrNull(page)?.timeStamp?.let { titleTimeFormat.format(it) } ?: ""
    }.stateIn(componentScope, SharingStarted.WhileSubscribed(5000), "")

    override val sideEffect = MutableSharedFlow<MediaBSHComponent.SideEffect>()
    override val optionsVisible = MutableStateFlow(false)
    override val visible = MutableStateFlow(false)

    override fun onShareClick() {
        componentScope.safeLaunch {
            sideEffect.emit(MediaBSHComponent.SideEffect.ShareMedia(getCurrentMedia()))
        }
    }

    override fun onTap() {
        optionsVisible.update { it.not() }
        titleHidingTimer()
    }

    override fun onTrashClick() {
        componentScope.safeLaunch {
            sideEffect.emit(MediaBSHComponent.SideEffect.TrashMedia(getCurrentMedia().uri))
        }
    }

    override fun onDismissRequest() {
        visible.update { false }
    }

    override fun onPageChanged(page: Int) {
        currentIndex.value = page
    }

    override fun setup(startIndex: Int) {
        componentScope.launch {
            currentIndex.update { startIndex }
            visible.update { true }
            sideEffect.emit(MediaBSHComponent.SideEffect.SetIndex(startIndex))
        }
    }

    override fun trashedSuccess() {
        // todo подумать что делать после удаления (MessageComponent)
    }

    private fun titleHidingTimer() {
        if (optionsVisible.value.not()) return
        titleHidingJob?.cancel()
        titleHidingJob = componentScope.safeLaunch {
            delay(TITLE_HIDING_TIMEOUT)
            optionsVisible.value = false
        }
    }

    private fun getCurrentMedia() = media.value[currentIndex.value]
}
