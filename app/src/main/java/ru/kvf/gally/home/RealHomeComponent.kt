package ru.kvf.gally.home

import android.content.res.Resources
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.Serializable
import ru.kvf.core.ComponentFactory
import ru.kvf.core.domain.usecase.EdgeToEdgeUseCase
import ru.kvf.core.utils.coroutineScope
import ru.kvf.createFavoriteComponent
import ru.kvf.createFoldersListComponent
import ru.kvf.createMediaListComponent
import ru.kvf.createSettingsListComponent
import ru.kvf.feature.favorite.FavoriteComponent
import ru.kvf.feature.folders.FoldersListComponent
import ru.kvf.core.R as CoreR

class RealHomeComponent(
    componentContext: ComponentContext,
    private val onOutput: (HomeComponent.Output) -> Unit,
    private val componentFactory: ComponentFactory,
    edgeToEdgeUseCase: EdgeToEdgeUseCase,
    resources: Resources
) : ComponentContext by componentContext, HomeComponent {
    private val navigation = StackNavigation<Config>()
    private val componentScope = coroutineScope()

    override val childStack: Value<ChildStack<*, HomeComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Media,
            handleBackButton = true,
            childFactory = ::child
        )

    private val edgeToEdgeEnable = edgeToEdgeUseCase.getEnabled()
        .stateIn(componentScope, SharingStarted.Eagerly, false)

    override val animatedBottomBar: StateFlow<Boolean>
        get() = TODO("Not yet implemented")

    override val animatedTopBar: StateFlow<Boolean>
        get() = TODO("Not yet implemented")

    override val title = childStack.map {
        resources.getString(
            when (it.active.instance) {
                HomeComponent.Child.Design -> CoreR.string.design
                is HomeComponent.Child.Favorite -> CoreR.string.favorite
                is HomeComponent.Child.Folders -> CoreR.string.folders
                is HomeComponent.Child.Media -> CoreR.string.media
                is HomeComponent.Child.Settings -> CoreR.string.settings
            }
        )
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

            FoldersListComponent.Output.OpenTrashRequested ->
                onOutput(HomeComponent.Output.OpenTrashRequested)
        }
    }

    private fun favoriteOutput(output: FavoriteComponent.Output) {
        when (output) {
            is FavoriteComponent.Output.OpenFolderRequested -> onOutput(
                HomeComponent.Output.OpenFolderRequested(output.name)
            )
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Media : Config

        @Serializable
        data object Folders : Config

        @Serializable
        data object Favorite : Config

        @Serializable
        data object Settings : Config

        @Serializable
        data object Design : Config
    }
}
