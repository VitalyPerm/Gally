package ru.kvf.gally.ui.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.parcelize.Parcelize
import ru.kvf.core.ComponentFactory
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.core.utils.componentCoroutineScope
import ru.kvf.gally.createHomeComponent
import ru.kvf.gally.ui.home.HomeComponent
import ru.kvf.photos.createPhotoComponent
import ru.kvf.photos.createPhotosListComponent
import ru.kvf.photos.ui.list.PhotosListComponent
import ru.kvf.settings.domain.ThemeUseCase

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    themeUseCase: ThemeUseCase,
) : ComponentContext by componentContext, RootComponent {

    private val navigation = StackNavigation<Config>()
    private val scope = componentCoroutineScope()

    override val theme = themeUseCase.getTheme().stateIn(
        scope,
        SharingStarted.Eagerly,
        ThemeType.System
    )

    override val childStack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Home,
            handleBackButton = true,
            childFactory = ::child
        )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            Config.Home -> RootComponent.Child.Home(
                componentFactory.createHomeComponent(componentContext, ::homeOutput)
            )

            is Config.Photo -> RootComponent.Child.Photo(
                componentFactory.createPhotoComponent(componentContext, config.index)
            )

            is Config.PhotosList -> RootComponent.Child.FolderPhotosList(
                componentFactory.createPhotosListComponent(componentContext, ::folderPhotosOutput, config.folderName)
            )
        }

    private fun homeOutput(output: HomeComponent.Output) {
        when (output) {
            is HomeComponent.Output.OpenPhotoRequested -> navigation.push(Config.Photo(output.index))
            is HomeComponent.Output.OpenFolderRequested -> navigation.push(Config.PhotosList(output.name))
        }
    }

    private fun folderPhotosOutput(output: PhotosListComponent.Output) {
        when (output) {
            is PhotosListComponent.Output.OpenPhotoRequested -> navigation.push(Config.Photo(output.index))
        }
    }

    private sealed interface Config : Parcelable {
        @Parcelize data object Home : Config

        @Parcelize data class Photo(val index: Int) : Config

        @Parcelize data class PhotosList(val folderName: String) : Config
    }
}
