package ru.kvf.photos.ui.detail

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import ru.kvf.core.domain.usecase.GetAllPhotosUseCase
import ru.kvf.core.domain.usecase.GetLikedPhotosUseCase
import ru.kvf.core.utils.Log
import ru.kvf.core.utils.componentCoroutineScope
import ru.kvf.core.utils.safeLaunch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RealPhotoComponent(
    componentContext: ComponentContext,
    override val startIndex: Int,
    reversed: Boolean = false,
    isFavoriteOnly: Boolean = false,
    folder: String? = null,
    getAllPhotosUseCase: GetAllPhotosUseCase,
    getLikedPhotosUseCase: GetLikedPhotosUseCase
) : ComponentContext by componentContext, PhotoComponent {

    private val scope = componentCoroutineScope()

    override val state = MutableStateFlow(PhotoState())

    private var titleHidingJob: Job? = null
    private val titleHidingTimeout = 5000L
    private val titleTimeFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    init {
        scope.safeLaunch {
            val photos = when {
                isFavoriteOnly -> getLikedPhotosUseCase().firstOrNull()
                folder != null -> getAllPhotosUseCase().firstOrNull()
                    ?.filter { it.folder == folder }
                else -> getAllPhotosUseCase().firstOrNull()
            } ?: return@safeLaunch

            state.update {
                state.value.copy(
                    photos = photos,
                    currentIndex = startIndex,
                    reversed = reversed
                )
            }
        }
    }

    override fun onPageChanged(page: Int) {
        val currentPhoto = state.value.photos[page]
        val photoDate = titleTimeFormat.format(Date(currentPhoto.timeStamp))
        state.update { state.value.copy(title = photoDate, currentIndex = page) }
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
