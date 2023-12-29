package ru.kvf.photos.ui.detail

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.usecase.GetAllPhotosUseCase
import ru.kvf.core.ui.VM
import ru.kvf.core.utils.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotoDetailsViewModel(
    getAllPhotosUseCase: GetAllPhotosUseCase,
    startIndex: Int
) : VM<PhotoDetailsState, Nothing>(PhotoDetailsState()) {

    private var titleHidingJob: Job? = null
    private val titleHidingTimeout = 5000L

    private val titleTimeFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    init {
        collectFlow(getAllPhotosUseCase()) { photos ->
            intent {
                reduce {
                    state.copy(
                        photos = photos,
                        currentIndex = startIndex,
                        loading = false
                    )
                }
            }
        }
    }

    fun rotate() {
        intent { reduce { state.copy(verticalOrientation = state.verticalOrientation.not()) } }
    }

    fun onSingleTap() = intent {
        reduce { state.copy(titleVisible = state.titleVisible.not()) }
        titleHidingTimer()
    }

    fun onPageChanged(page: Int) = intent {
        Log.d("onPageChanged = $page ")
        val currentPhoto = state.photos[page]
        val photoDate = titleTimeFormat.format(Date(currentPhoto.timeStamp))
        reduce { state.copy(title = photoDate, currentIndex = page) }
    }

    private fun titleHidingTimer() = intent {
        if (state.titleVisible.not()) return@intent
        titleHidingJob?.cancel()
        titleHidingJob = intent {
            delay(titleHidingTimeout)
            reduce { state.copy(titleVisible = false) }
        }
    }
}
