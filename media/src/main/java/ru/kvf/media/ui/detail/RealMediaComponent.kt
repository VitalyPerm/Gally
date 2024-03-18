package ru.kvf.media.ui.detail

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
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

    private val componentScope = lifecycle.coroutineScope()

    override val media = MutableStateFlow(emptyList<Media>())
    override val title = MutableStateFlow("")
    override val titleVisible = MutableStateFlow(false)

    private var titleHidingJob: Job? = null
    private val titleHidingTimeout = 5000L
    private val titleTimeFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

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
        val currentMedia = media.value[page]
        val mediaDate = titleTimeFormat.format(Date(currentMedia.timeStamp))
        title.value = mediaDate
    }

    override fun onSingleTap() {
        titleVisible.update { it.not() }
        titleHidingTimer()
    }

    private fun titleHidingTimer() {
        if (titleVisible.value.not()) return
        titleHidingJob?.cancel()
        titleHidingJob = componentScope.safeLaunch {
            delay(titleHidingTimeout)
            titleVisible.value = false
        }
    }
}
