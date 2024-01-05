package ru.kvf.photos.ui.details

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.usecase.GetAllPhotosUseCase
import ru.kvf.core.ui.VM

class PhotoDetailsViewModel(
    getAllPhotosUseCase: GetAllPhotosUseCase,
    photoId: Long,
) : VM<PhotoDetailsState, DetailsSideEffect>(PhotoDetailsState()) {

    init {
        collectFlow(getAllPhotosUseCase()) { photos ->
            val index = photos.indexOfFirst { it.id == photoId }.takeIf { it != -1 } ?: 0
            intent {
                reduce {
                    state.copy(
                        photos = photos,
                        startIndex = index,
                        loading = false
                    )
                }
            }
        }
    }
}
