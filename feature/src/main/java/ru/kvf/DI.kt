package ru.kvf

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.get
import ru.kvf.core.ComponentFactory
import ru.kvf.core.domain.entities.Media
import ru.kvf.feature.favorite.FavoriteComponent
import ru.kvf.feature.favorite.RealFavoriteComponent
import ru.kvf.feature.favorite.folders.FavoriteFoldersComponent
import ru.kvf.feature.favorite.folders.RealFavoriteFoldersComponent
import ru.kvf.feature.favorite.media.FavoriteMediaComponent
import ru.kvf.feature.favorite.media.RealFavoriteMediaComponent
import ru.kvf.feature.folders.FoldersListComponent
import ru.kvf.feature.folders.RealFoldersListComponent
import ru.kvf.feature.media.MediaListComponent
import ru.kvf.feature.media.RealMediaListComponent
import ru.kvf.feature.mediabsh.MediaBSHComponent
import ru.kvf.feature.mediabsh.RealMediaBSHComponent
import ru.kvf.feature.settings.RealSettingsListComponent
import ru.kvf.feature.settings.SettingsListComponent

fun ComponentFactory.createMediaListComponent(
    componentContext: ComponentContext,
    folderName: String? = null,
): MediaListComponent = RealMediaListComponent(
    componentContext = componentContext,
    folderName = folderName,
    getSortedMediaUseCase = get(),
    getFolderMediaUseCase = get(),
    getFavoriteMediaIdsUseCase = get(),
    gridCellsCountChangeUseCase = get(),
    handleFavoriteClickUseCase = get(),
    componentFactory = get(),
    context = get()
)

fun ComponentFactory.createFavoriteComponent(
    componentContext: ComponentContext,
    output: (FavoriteComponent.Output) -> Unit
): FavoriteComponent = RealFavoriteComponent(
    componentContext,
    componentFactory = get(),
    onOutput = output
)

fun ComponentFactory.createFavoriteFoldersComponent(
    componentContext: ComponentContext,
    output: (FavoriteFoldersComponent.Output) -> Unit
): FavoriteFoldersComponent = RealFavoriteFoldersComponent(
    componentContext = componentContext,
    onOutput = output,
    handleFolderDoubleClickUseCase = get(),
    getFavoriteFoldersUseCase = get()
)

fun ComponentFactory.createFavoriteMediaComponent(
    componentContext: ComponentContext,
): FavoriteMediaComponent = RealFavoriteMediaComponent(
    componentContext = componentContext,
    getFavoriteMediaUseCase = get(),
    handleMediaFavoriteClickUseCase = get(),
    componentFactory = get()
)

fun ComponentFactory.createFoldersListComponent(
    componentContext: ComponentContext,
    output: (FoldersListComponent.Output) -> Unit
): FoldersListComponent = RealFoldersListComponent(
    componentContext = componentContext,
    onOutput = output,
    getFoldersUseCase = get(),
    gridCellsCountChangeUseCase = get(),
    handleFolderDoubleClickUseCase = get(),
    getFavoriteFoldersIdsUseCase = get()
)

fun ComponentFactory.createSettingsListComponent(
    componentContext: ComponentContext
): SettingsListComponent = RealSettingsListComponent(
    componentContext = componentContext,
    themeUseCase = get(),
    edgeUseCase = get(),
    sortByUseCase = get(),
    loadMediaUseCase = get()
)

fun ComponentFactory.createMediaBSHComponent(
    componentContext: ComponentContext,
    media: StateFlow<List<Media>>,
): MediaBSHComponent = RealMediaBSHComponent(
    componentContext = componentContext,
    media = media
)
