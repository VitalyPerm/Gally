package ru.kvf.gally.ui.home

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.design.DesignComponent
import ru.kvf.favorite.ui.FavoriteListComponent
import ru.kvf.folders.ui.folderlist.FoldersListComponent
import ru.kvf.photos.ui.list.PhotosListComponent
import ru.kvf.settings.ui.list.SettingsListComponent

interface HomeComponent {
    val state: StateFlow<HomeState>
    val sideEffect: SharedFlow<RootSideEffect>

    enum class Page {
        Photos, Folders, Favorite, Settings, Design
    }

    val childStack: Value<ChildStack<*, Child>>

    fun onPageSelected(page: Page)

    sealed interface Child {
        class Photos(val component: PhotosListComponent) : Child
        class Folders(val component: FoldersListComponent) : Child
        class Favorite(val component: FavoriteListComponent) : Child
        class Settings(val component: SettingsListComponent) : Child
        class Design(val component: DesignComponent) : Child
    }

    sealed interface Output {
        data class OpenPhotoRequested(
            val index: Int,
            val reversed: Boolean,
            val isFavoriteOnly: Boolean = false
        ) : Output
        data class OpenFolderRequested(val name: String) : Output
    }
}
