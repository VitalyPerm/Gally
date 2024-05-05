package ru.kvf.feature.favorite.folders

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kvf.core.domain.usecase.favorite.GetFavoriteFoldersUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFolderFavoriteClickUseCase
import ru.kvf.core.utils.FolderList
import ru.kvf.core.utils.coroutineScope

class RealFavoriteFoldersComponent(
    componentContext: ComponentContext,
    private val onOutput: (FavoriteFoldersComponent.Output) -> Unit,
    getFavoriteFoldersUseCase: GetFavoriteFoldersUseCase,
    private val handleFolderFavoriteClickUseCase: HandleFolderFavoriteClickUseCase
) : ComponentContext by componentContext, FavoriteFoldersComponent {

    private val componentScope = coroutineScope()

    override val folders: StateFlow<FolderList> = getFavoriteFoldersUseCase()
        .stateIn(componentScope, SharingStarted.Eagerly, FolderList.EMPTY)

    override fun onFolderClick(name: String) {
        onOutput(FavoriteFoldersComponent.Output.OpenFolderRequested(name))
    }

    override fun onFolderLongClick(id: Long) {
        componentScope.launch { handleFolderFavoriteClickUseCase(id) }
    }
}
