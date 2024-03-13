package ru.kvf.gally.ui.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.gally.ui.home.HomeComponent
import ru.kvf.media.ui.detail.MediaComponent
import ru.kvf.media.ui.list.MediaListComponent

interface RootComponent {

    val theme: StateFlow<ThemeType>

    val childStack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
        class Media(val component: MediaComponent) : Child
        class FolderMediaList(val component: MediaListComponent) : Child
    }
}
