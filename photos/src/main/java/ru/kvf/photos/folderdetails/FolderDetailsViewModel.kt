package ru.kvf.photos.folderdetails

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.usecase.GetFolderPhotosUseCase
import ru.kvf.core.ui.VM

class FolderDetailsViewModel(
    folderName: String,
    getFolderPhotosUseCase: GetFolderPhotosUseCase
) : VM<FolderDetailsState, FolderDetailsSideEffect>(FolderDetailsState()) {
    init {
        collectFlow(getFolderPhotosUseCase(folderName)) { photos ->
            intent {
                reduce {
                    state.copy(
                        photos = photos,
                        loading = false
                    )
                }
            }
        }
    }
}
