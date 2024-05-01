package ru.kvf.gally.home

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
import ru.kvf.core.domain.usecase.EdgeToEdgeUseCase
import ru.kvf.core.domain.usecase.LoadMediaUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.createFavoriteComponent
import ru.kvf.createFoldersListComponent
import ru.kvf.createMediaListComponent
import ru.kvf.createSettingsListComponent
import ru.kvf.feature.favorite.FavoriteComponent
import ru.kvf.feature.folders.FoldersListComponent

class RealHomeComponent(
    componentContext: ComponentContext,
    private val onOutput: (HomeComponent.Output) -> Unit,
    private val componentFactory: ComponentFactory,
    private val loadMediaUseCase: LoadMediaUseCase,
    edgeToEdgeUseCase: EdgeToEdgeUseCase,
    performHapticFeedBackUseCase: PerformHapticFeedBackUseCase,
) : ComponentContext by componentContext, HomeComponent {
    private val navigation = StackNavigation<Config>()
    private val componentScope = coroutineScope()

    override val childStack: Value<ChildStack<*, HomeComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Media,
            handleBackButton = true,
            childFactory = ::child
        )

    override val state = MutableStateFlow(HomeState())
    override val sideEffect = MutableSharedFlow<RootSideEffect>()

    init {
        componentScope.collectFlow(edgeToEdgeUseCase.getEnabled()) { edgeToEdgeEnable ->
            state.update { state.value.copy(edgeToEdgeEnable = edgeToEdgeEnable) }
        }

        componentScope.collectFlow(performHapticFeedBackUseCase.collect()) {
            componentScope.launch { sideEffect.emit(RootSideEffect.Vibrate) }
        }

        lifecycle.doOnPause { state.update { state.value.copy(loading = true) } }
        lifecycle.doOnResume {
            componentScope.safeLaunch {
                loadMediaUseCase()
                state.update { state.value.copy(loading = false) }
            }
        }
    }

    private fun child(config: Config, componentContext: ComponentContext): HomeComponent.Child =
        when (config) {
            Config.Media -> HomeComponent.Child.Media(
                componentFactory.createMediaListComponent(componentContext)
            )

            Config.Folders -> HomeComponent.Child.Folders(
                componentFactory.createFoldersListComponent(componentContext, ::foldersListOutput)
            )

            Config.Favorite -> HomeComponent.Child.Favorite(
                componentFactory.createFavoriteComponent(
                    componentContext = componentContext,
                    output = ::favoriteOutput
                )
            )

            Config.Settings -> HomeComponent.Child.Settings(
                componentFactory.createSettingsListComponent(componentContext)
            )

            Config.Design -> HomeComponent.Child.Design
        }

    override fun onPageSelected(page: HomeComponent.Page) {
        val newConfig = when (page) {
            HomeComponent.Page.Media -> Config.Media
            HomeComponent.Page.Folders -> Config.Folders
            HomeComponent.Page.Favorite -> Config.Favorite
            HomeComponent.Page.Settings -> Config.Settings
            HomeComponent.Page.Design -> Config.Design
        }
        navigation.bringToFront(newConfig)
    }

    private fun foldersListOutput(output: FoldersListComponent.Output) {
        when (output) {
            is FoldersListComponent.Output.OpenFolderRequested -> onOutput(
                HomeComponent.Output.OpenFolderRequested(output.name)
            )
        }
    }

    private fun favoriteOutput(output: FavoriteComponent.Output) {
        when (output) {
            is FavoriteComponent.Output.OpenFolderRequested -> onOutput(
                HomeComponent.Output.OpenFolderRequested(output.name)
            )
        }
    }

    private sealed interface Config : Parcelable {
        @Parcelize data object Media : Config

        @Parcelize data object Folders : Config

        @Parcelize data object Favorite : Config

        @Parcelize data object Settings : Config

        @Parcelize data object Design : Config
    }
}
