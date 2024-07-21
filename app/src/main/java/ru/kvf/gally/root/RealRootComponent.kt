package ru.kvf.gally.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
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
import ru.kvf.createMediaDetailsComponent
import ru.kvf.createMediaListComponent
import ru.kvf.createTrashComponent
import ru.kvf.createVideoPlayerComponent
import ru.kvf.feature.folders.details.FolderDetailsComponent
import ru.kvf.feature.media.ui.MediaListComponent
import ru.kvf.feature.mediadetails.MediaDetailsComponent
import ru.kvf.feature.trash.TrashComponent
import ru.kvf.feature.video.VideoPlayerComponent
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

    override fun onBackClicked() {
        navigation.pop()
    }

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
                componentFactory.createMediaListComponent(componentContext, ::mediaListOutput)
            )

            Config.Trash -> RootComponent.Child.Trash(
                componentFactory.createTrashComponent(componentContext, ::trashOutput)
            )

            is Config.FolderDetails -> RootComponent.Child.FolderDetails(
                componentFactory.createFolderDetailsComponent(
                    componentContext,
                    ::folderDetailsOutput,
                    config.folderName
                )
            )

            is Config.MediaDetails -> RootComponent.Child.MediaDetails(
                componentFactory.createMediaDetailsComponent(
                    componentContext,
                    ::mediaDetailsOutput,
                    config.type,
                    config.initialMediaId
                )
            )

            is Config.VideoPlayer -> RootComponent.Child.VideoPlayer(
                componentFactory.createVideoPlayerComponent(
                    componentContext,
                    ::videoPlayerOutput,
                    config.mediaId
                )
            )
        }

    private fun homeOutput(output: HomeComponent.Output) {
        when (output) {
            is HomeComponent.Output.OpenFolderRequested -> navigation.push(
                Config.FolderDetails(output.name)
            )
            HomeComponent.Output.OpenTrashRequested -> navigation.push(Config.Trash)
            is HomeComponent.Output.MediaDetailsRequested -> navigation.push(
                Config.MediaDetails(output.type, output.mediaId)
            )

            is HomeComponent.Output.VideoRequested -> navigation.push(Config.VideoPlayer(output.mediaId))
        }
    }

    private fun mediaListOutput(output: MediaListComponent.Output) {
        when (output) {
            is MediaListComponent.Output.MediaDetailsRequested ->
                navigation.push(Config.MediaDetails(MediaDetailsComponent.Type.All, output.mediaId))

            is MediaListComponent.Output.VideoRequested -> {}
        }
    }

    private fun trashOutput(output: TrashComponent.Output) {
        when (output) {
            is TrashComponent.Output.MediaDetailsRequested -> navigation.push(
                Config.MediaDetails(
                    MediaDetailsComponent.Type.Trash,
                    output.mediaId
                )
            )
        }
    }

    private fun folderDetailsOutput(output: FolderDetailsComponent.Output) {
        when (output) {
            is FolderDetailsComponent.Output.MediaDetailsRequested -> navigation.push(
                Config.MediaDetails(
                    MediaDetailsComponent.Type.Folder(output.folderName),
                    output.mediaId
                )
            )
        }
    }

    private fun videoPlayerOutput(output: VideoPlayerComponent.Output) {
        when (output) {
            VideoPlayerComponent.Output.CloseRequested -> navigation.pop()
        }
    }

    private fun mediaDetailsOutput(output: MediaDetailsComponent.Output) {
        when (output) {
            is MediaDetailsComponent.Output.VideoPlayerRequested ->
                navigation.push(Config.VideoPlayer(output.videoId))
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

        @Serializable
        data class MediaDetails(
            val type: MediaDetailsComponent.Type,
            val initialMediaId: Long
        ) : Config

        @Serializable
        data class VideoPlayer(val mediaId: Long) : Config
    }
}
