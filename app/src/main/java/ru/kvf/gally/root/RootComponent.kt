package ru.kvf.gally.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.feature.media.MediaListComponent
import ru.kvf.feature.trash.TrashComponent
import ru.kvf.gally.home.HomeComponent

interface RootComponent {

    val theme: StateFlow<ThemeType>
    val childStack: Value<ChildStack<*, Child>>
    val sideEffect: Flow<SideEffect>

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
        class FolderMediaList(val component: MediaListComponent) : Child
        class Trash(val component: TrashComponent) : Child
    }

    sealed interface SideEffect {
        data object Vibrate : SideEffect
    }
}
