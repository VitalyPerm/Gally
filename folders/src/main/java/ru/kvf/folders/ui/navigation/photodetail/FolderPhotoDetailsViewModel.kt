package ru.kvf.folders.ui.navigation.photodetail

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.ui.VM
import ru.kvf.folders.domain.GetFolderPhotosUseCase

class FolderPhotoDetailsViewModel(
    folderName: String,
    photoId: Long,
    getFolderPhotosUseCase: GetFolderPhotosUseCase
) : VM<FolderPhotoDetailsState, Nothing>(FolderPhotoDetailsState()) {

    init {
        collectFlow(getFolderPhotosUseCase.invoke(folderName)) { photos ->
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
