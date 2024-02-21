package ru.kvf.gally.ui.home

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnPause
import com.arkivanov.essenty.lifecycle.doOnResume
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import ru.kvf.core.ComponentFactory
import ru.kvf.core.domain.usecase.LoadPhotosUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.componentCoroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.design.RealDesignComponent
import ru.kvf.favorite.createFavoriteListComponent
import ru.kvf.folders.createFoldersListComponent
import ru.kvf.folders.ui.folderlist.FoldersListComponent
import ru.kvf.photos.createPhotosListComponent
import ru.kvf.photos.ui.list.PhotosListComponent
import ru.kvf.settings.createSettingsListComponent
import ru.kvf.settings.domain.EdgeToEdgeUseCase

class RealHomeComponent(
    componentContext: ComponentContext,
    private val onOutput: (HomeComponent.Output) -> Unit,
    private val componentFactory: ComponentFactory,
    private val loadPhotosUseCase: LoadPhotosUseCase,
    edgeToEdgeUseCase: EdgeToEdgeUseCase,
    performHapticFeedBackUseCase: PerformHapticFeedBackUseCase,
) : ComponentContext by componentContext, HomeComponent {
    private val navigation = StackNavigation<Config>()
    private val scope = componentCoroutineScope()

    override val childStack: Value<ChildStack<*, HomeComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Photos,
            handleBackButton = true,
            childFactory = ::child
        )

    override val state = MutableStateFlow(HomeState())
    override val sideEffect = MutableSharedFlow<RootSideEffect>()

    init {
        scope.collectFlow(edgeToEdgeUseCase.getEnabled()) { edgeToEdgeEnable ->
            state.update { state.value.copy(edgeToEdgeEnable = edgeToEdgeEnable) }
        }

        scope.collectFlow(performHapticFeedBackUseCase.collect()) {
            scope.launch { sideEffect.emit(RootSideEffect.Vibrate) }
        }

        lifecycle.doOnPause { state.update { state.value.copy(loading = true) } }
        lifecycle.doOnResume {
            scope.safeLaunch {
                loadPhotosUseCase()
                state.update { state.value.copy(loading = false) }
            }
        }
    }

    private fun child(config: Config, componentContext: ComponentContext): HomeComponent.Child =
        when (config) {
            Config.Photos -> HomeComponent.Child.Photos(
                componentFactory.createPhotosListComponent(componentContext, ::photosListOutput)
            )

            Config.Folders -> HomeComponent.Child.Folders(
                componentFactory.createFoldersListComponent(componentContext, ::foldersListOutput)
            )

            Config.Favorite -> HomeComponent.Child.Favorite(
                componentFactory.createFavoriteListComponent(componentContext)
            )

            Config.Settings -> HomeComponent.Child.Settings(
                componentFactory.createSettingsListComponent(componentContext)
            )

            Config.Design -> HomeComponent.Child.Design(RealDesignComponent(componentContext))
        }

    private fun photosListOutput(output: PhotosListComponent.Output) {
        when (output) {
            is PhotosListComponent.Output.OpenPhotoRequested -> onOutput(
                HomeComponent.Output.OpenPhotoRequested(output.index)
            )
        }
    }

    private fun foldersListOutput(output: FoldersListComponent.Output) {
        when (output) {
            is FoldersListComponent.Output.OpenFolderRequested -> onOutput(
                HomeComponent.Output.OpenFolderRequested(output.name)
            )
        }
    }

    override fun onPageSelected(page: HomeComponent.Page) {
        val newConfig = when (page) {
            HomeComponent.Page.Photos -> Config.Photos
            HomeComponent.Page.Folders -> Config.Folders
            HomeComponent.Page.Favorite -> Config.Favorite
            HomeComponent.Page.Settings -> Config.Settings
            HomeComponent.Page.Design -> Config.Design
        }
        navigation.bringToFront(newConfig)
    }

    private sealed interface Config : Parcelable {
        @Parcelize data object Photos : Config

        @Parcelize data object Folders : Config

        @Parcelize data object Favorite : Config

        @Parcelize data object Settings : Config

        @Parcelize data object Design : Config
    }
}
