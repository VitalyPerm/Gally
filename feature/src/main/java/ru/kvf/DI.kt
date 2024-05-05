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
    componentContext,
    folderName,
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get()
)

fun ComponentFactory.createFavoriteComponent(
    componentContext: ComponentContext,
    output: (FavoriteComponent.Output) -> Unit
): FavoriteComponent = RealFavoriteComponent(
    componentContext,
    get(),
    output,
    get()
)

fun ComponentFactory.createFavoriteFoldersComponent(
    componentContext: ComponentContext,
    output: (FavoriteFoldersComponent.Output) -> Unit
): FavoriteFoldersComponent = RealFavoriteFoldersComponent(
    componentContext,
    output,
    get(),
    get()
)

fun ComponentFactory.createFavoriteMediaComponent(
    componentContext: ComponentContext,
): FavoriteMediaComponent = RealFavoriteMediaComponent(
    componentContext,
    get(),
    get(),
    get()
)

fun ComponentFactory.createFoldersListComponent(
    componentContext: ComponentContext,
    output: (FoldersListComponent.Output) -> Unit
): FoldersListComponent = RealFoldersListComponent(
    componentContext,
    output,
    get(),
    get(),
    get(),
    get()
)

fun ComponentFactory.createSettingsListComponent(
    componentContext: ComponentContext
): SettingsListComponent = RealSettingsListComponent(
    componentContext,
    get(),
    get(),
    get(),
    get()
)

fun ComponentFactory.createMediaBSHComponent(
    componentContext: ComponentContext,
    media: StateFlow<List<Media>>,
): MediaBSHComponent = RealMediaBSHComponent(
    componentContext,
    media,
    get(),
    get(),
)
