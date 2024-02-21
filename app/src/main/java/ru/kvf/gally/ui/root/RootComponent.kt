package ru.kvf.gally.ui.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.gally.ui.home.HomeComponent
import ru.kvf.photos.ui.detail.PhotoComponent
import ru.kvf.photos.ui.list.PhotosListComponent

interface RootComponent {

    val theme: StateFlow<ThemeType>

    val childStack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
        class Photo(val component: PhotoComponent) : Child
        class FolderPhotosList(val component: PhotosListComponent) : Child
    }
}
