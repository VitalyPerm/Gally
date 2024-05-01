package ru.kvf.gally.root

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
import ru.kvf.core.utils.coroutineScope
import ru.kvf.createMediaListComponent
import ru.kvf.gally.createHomeComponent
import ru.kvf.gally.home.HomeComponent
import ru.kvf.core.domain.usecase.ThemeUseCase

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    themeUseCase: ThemeUseCase,
) : ComponentContext by componentContext, RootComponent {

    private val navigation = StackNavigation<Config>()
    private val componentScope = coroutineScope()

    override val theme = themeUseCase.getTheme().stateIn(
        componentScope,
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

            is Config.MediaList -> RootComponent.Child.FolderMediaList(
                componentFactory.createMediaListComponent(componentContext, config.folderName)
            )
        }

    private fun homeOutput(output: HomeComponent.Output) {
        when (output) {
            is HomeComponent.Output.OpenFolderRequested -> navigation.push(Config.MediaList(output.name))
        }
    }

    private sealed interface Config : Parcelable {
        @Parcelize data object Home : Config

        @Parcelize data class MediaList(val folderName: String) : Config
    }
}
