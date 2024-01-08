package ru.kvf.folders.ui.navigation.folderphotolist

import kotlinx.coroutines.delay
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.ui.VM
import ru.kvf.core.utils.Constants
import ru.kvf.folders.domain.GetFolderPhotosUseCase

class FolderPhotosListViewModel(
    folderName: String,
    getFolderPhotosUseCase: GetFolderPhotosUseCase,
    getLikedIdsListUseCase: GetLikedIdsListUseCase,
    private val handleLikeClickUseCase: HandleLikeClickUseCase
) : VM<FolderPhotosListState, Nothing>(FolderPhotosListState()) {
    init {
        collectFlow(getFolderPhotosUseCase.sorted(folderName)) { photos ->
            intent {
                reduce {
                    state.copy(photos = photos, loading = false)
                }
            }
        }

        collectFlow(getLikedIdsListUseCase()) { ids ->
            intent {
                reduce { state.copy(likedPhotos = ids) }
            }
        }
    }

    fun onLikeClick(id: Long) = intent {
        delay(Constants.PHOTO_ITEM_LIKE_DURATION)
        handleLikeClickUseCase(id)
    }
}
