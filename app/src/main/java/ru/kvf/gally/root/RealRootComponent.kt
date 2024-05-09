package ru.kvf.gally.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnResume
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import ru.kvf.core.ComponentFactory
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.core.domain.usecase.LoadMediaUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.domain.usecase.ThemeUseCase
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.createMediaListComponent
import ru.kvf.createTrashComponent
import ru.kvf.gally.createHomeComponent
import ru.kvf.gally.home.HomeComponent

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    themeUseCase: ThemeUseCase,
    performHapticFeedBackUseCase: PerformHapticFeedBackUseCase,
    private val loadMediaUseCase: LoadMediaUseCase
) : ComponentContext by componentContext, RootComponent {

    private val navigation = StackNavigation<Config>()
    private val componentScope = coroutineScope()

    override val theme = themeUseCase.getTheme().stateIn(
        componentScope,
        SharingStarted.Eagerly,
        ThemeType.System
    )

    override val sideEffect = MutableSharedFlow<RootComponent.SideEffect>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Home,
            handleBackButton = true,
            childFactory = ::child
        )

    init {
        componentScope.collectFlow(performHapticFeedBackUseCase.collect()) {
            componentScope.launch { sideEffect.emit(RootComponent.SideEffect.Vibrate) }
        }
        lifecycle.doOnResume { componentScope.safeLaunch { loadMediaUseCase() } }
    }

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            Config.Home -> RootComponent.Child.Home(
                componentFactory.createHomeComponent(componentContext, ::homeOutput)
            )

            is Config.MediaList -> RootComponent.Child.FolderMediaList(
                componentFactory.createMediaListComponent(componentContext, config.folderName)
            )

            Config.Trash -> RootComponent.Child.Trash(
                componentFactory.createTrashComponent(componentContext)
            )
        }

    private fun homeOutput(output: HomeComponent.Output) {
        when (output) {
            is HomeComponent.Output.OpenFolderRequested -> navigation.push(Config.MediaList(output.name))
            HomeComponent.Output.OpenTrashRequested -> navigation.push(Config.Trash)
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Home : Config

        @Serializable
        data class MediaList(val folderName: String) : Config

        @Serializable
        data object Trash : Config
    }
}
