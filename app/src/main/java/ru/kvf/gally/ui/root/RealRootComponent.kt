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
import ru.kvf.core.utils.coroutineScope
import ru.kvf.gally.createHomeComponent
import ru.kvf.gally.ui.home.HomeComponent
import ru.kvf.media.createMediaComponent
import ru.kvf.media.createMediaListComponent
import ru.kvf.media.ui.list.MediaListComponent
import ru.kvf.settings.domain.ThemeUseCase

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    themeUseCase: ThemeUseCase,
) : ComponentContext by componentContext, RootComponent {

    private val navigation = StackNavigation<Config>()
    private val componentScope = lifecycle.coroutineScope()

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

            is Config.Media -> RootComponent.Child.Media(
                componentFactory.createMediaComponent(
                    componentContext = componentContext,
                    startIndex = config.index,
                    isReversed = config.reversed,
                    isFavoriteOnly = config.isFavoriteOnly,
                    folder = config.folder
                )
            )

            is Config.MediaList -> RootComponent.Child.FolderMediaList(
                componentFactory.createMediaListComponent(componentContext, ::folderMediaOutput, config.folderName)
            )
        }

    private fun homeOutput(output: HomeComponent.Output) {
        when (output) {
            is HomeComponent.Output.OpenMediaRequested -> {
                navigation.push(
                    Config.Media(
                        index = output.index,
                        reversed = output.reversed,
                        isFavoriteOnly = output.isFavoriteOnly
                    )
                )
            }
            is HomeComponent.Output.OpenFolderRequested -> navigation.push(Config.MediaList(output.name))
        }
    }

    private fun folderMediaOutput(output: MediaListComponent.Output) {
        when (output) {
            is MediaListComponent.Output.OpenMediaRequested -> navigation.push(
                Config.Media(
                    index = output.index,
                    reversed = output.reversed,
                    folder = output.folder
                )
            )
        }
    }

    private sealed interface Config : Parcelable {
        @Parcelize data object Home : Config

        @Parcelize data class Media(
            val index: Int,
            val reversed: Boolean,
            val isFavoriteOnly: Boolean = false,
            val folder: String? = null
        ) : Config

        @Parcelize data class MediaList(val folderName: String) : Config
    }
}
