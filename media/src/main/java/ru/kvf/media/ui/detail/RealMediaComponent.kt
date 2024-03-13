package ru.kvf.media.ui.detail

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import ru.kvf.core.domain.usecase.GetLikedMediaUseCase
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.utils.componentCoroutineScope
import ru.kvf.core.utils.safeLaunch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RealMediaComponent(
    componentContext: ComponentContext,
    override val startIndex: Int,
    reversed: Boolean = false,
    isFavoriteOnly: Boolean = false,
    folder: String? = null,
    getMediaUseCase: GetMediaUseCase,
    getLikedMediaUseCase: GetLikedMediaUseCase,
) : ComponentContext by componentContext, MediaComponent {

    private val scope = componentCoroutineScope()

    override val state = MutableStateFlow(MediaState())

    private var titleHidingJob: Job? = null
    private val titleHidingTimeout = 5000L
    private val titleTimeFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    init {
        scope.safeLaunch {
            val media = when {
                isFavoriteOnly -> getLikedMediaUseCase().firstOrNull()
                folder != null -> getMediaUseCase().firstOrNull()
                    ?.filter { it.folder == folder }
                else -> getMediaUseCase().firstOrNull()
            } ?: return@safeLaunch

            state.update {
                state.value.copy(
                    media = media,
                    currentIndex = startIndex,
                    reversed = reversed
                )
            }
        }
    }

    override fun onPageChanged(page: Int) {
        val currentMedia = state.value.media[page]
        val mediaDate = titleTimeFormat.format(Date(currentMedia.timeStamp))
        state.update { state.value.copy(title = mediaDate, currentIndex = page) }
    }

    override fun onSingleTap() {
        state.update { state.value.copy(titleVisible = state.value.titleVisible.not()) }
        titleHidingTimer()
    }

    private fun titleHidingTimer() {
        if (state.value.titleVisible.not()) return
        titleHidingJob?.cancel()
        titleHidingJob = scope.safeLaunch {
            delay(titleHidingTimeout)
            state.update { state.value.copy(titleVisible = false) }
        }
    }
}
