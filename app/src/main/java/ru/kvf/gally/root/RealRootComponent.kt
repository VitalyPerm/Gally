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
import ru.kvf.core.domain.usecase.DeleteMediaUseCase
import ru.kvf.core.domain.usecase.LoadMediaUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.domain.usecase.ShareMediaUseCase
import ru.kvf.core.domain.usecase.ThemeUseCase
import ru.kvf.core.domain.usecase.TrashMediaUseCase
import ru.kvf.core.utils.collectSafe
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.createFolderDetailsComponent
import ru.kvf.createMediaListComponent
import ru.kvf.createTrashComponent
import ru.kvf.gally.createHomeComponent
import ru.kvf.gally.home.HomeComponent

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    themeUseCase: ThemeUseCase,
    performHapticFeedBackUseCase: PerformHapticFeedBackUseCase,
    shareMediaUseCase: ShareMediaUseCase,
    loadMediaUseCase: LoadMediaUseCase,
    trashMediaUseCase: TrashMediaUseCase,
    deleteMediaUseCase: DeleteMediaUseCase
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
        componentScope.collectSafe(performHapticFeedBackUseCase.collect()) {
            componentScope.launch { sideEffect.emit(RootComponent.SideEffect.Vibrate) }
        }

        componentScope.collectSafe(shareMediaUseCase.collect()) {
            componentScope.launch { sideEffect.emit(RootComponent.SideEffect.ShareMedia(it)) }
        }

        componentScope.collectSafe(trashMediaUseCase.collect()) {
            componentScope.launch {
                sideEffect.emit(
                    RootComponent.SideEffect.TrashMedia(
                        it.first,
                        it.second
                    )
                )
            }
        }

        componentScope.collectSafe(deleteMediaUseCase.collect()) {
            componentScope.launch { sideEffect.emit(RootComponent.SideEffect.DeleteMedia(it)) }
        }

        lifecycle.doOnResume { componentScope.safeLaunch { loadMediaUseCase() } }
    }

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            Config.Home -> RootComponent.Child.Home(
                componentFactory.createHomeComponent(componentContext, ::homeOutput)
            )

            is Config.MediaList -> RootComponent.Child.FolderMediaList(
                componentFactory.createMediaListComponent(componentContext)
            )

            Config.Trash -> RootComponent.Child.Trash(
                componentFactory.createTrashComponent(componentContext)
            )

            is Config.FolderDetails -> RootComponent.Child.FolderDetails(
                componentFactory.createFolderDetailsComponent(componentContext, config.folderName)
            )
        }

    private fun homeOutput(output: HomeComponent.Output) {
        when (output) {
            is HomeComponent.Output.OpenFolderRequested -> navigation.push(
                Config.FolderDetails(
                    output.name
                )
            )
            HomeComponent.Output.OpenTrashRequested -> navigation.push(Config.Trash)
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Home : Config

        @Serializable
        data object MediaList : Config

        @Serializable
        data class FolderDetails(val folderName: String) : Config

        @Serializable
        data object Trash : Config
    }
}
