package ru.kvf.gally.home

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.feature.favorite.FavoriteComponent
import ru.kvf.feature.folders.FoldersListComponent
import ru.kvf.feature.media.MediaListComponent
import ru.kvf.feature.settings.SettingsListComponent

interface HomeComponent {
    val edgeToEdgeEnable: StateFlow<Boolean>

    enum class Page {
        Media, Folders, Favorite, Settings, Design
    }

    val childStack: Value<ChildStack<*, Child>>

    fun onPageSelected(page: Page)

    sealed interface Child {
        class Media(val component: MediaListComponent) : Child
        class Folders(val component: FoldersListComponent) : Child
        class Favorite(val component: FavoriteComponent) : Child
        class Settings(val component: SettingsListComponent) : Child
        data object Design : Child
    }

    sealed interface Output {
        data class OpenFolderRequested(val name: String) : Output
        data object OpenTrashRequested : Output
    }
}
