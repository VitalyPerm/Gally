package ru.kvf

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.kvf.core.ComponentFactory
import ru.kvf.feature.favorite.FavoriteComponent
import ru.kvf.feature.favorite.RealFavoriteComponent
import ru.kvf.feature.favorite.folders.FavoriteFoldersComponent
import ru.kvf.feature.favorite.folders.RealFavoriteFoldersComponent
import ru.kvf.feature.favorite.media.FavoriteMediaComponent
import ru.kvf.feature.favorite.media.RealFavoriteMediaComponent
import ru.kvf.feature.media.MediaListComponent
import ru.kvf.feature.media.RealMediaListComponent

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
    handleMediaDoubleClickUseCase = get(),
    componentFactory = get(),
    context = get()
)

fun ComponentFactory.createFavoriteComponent(
    componentContext: ComponentContext,
): FavoriteComponent = RealFavoriteComponent(componentContext, get())

fun ComponentFactory.createFavoriteFoldersComponent(
    componentContext: ComponentContext,
): FavoriteFoldersComponent = RealFavoriteFoldersComponent(componentContext)

fun ComponentFactory.createFavoriteMediaComponent(
    componentContext: ComponentContext,
): FavoriteMediaComponent = RealFavoriteMediaComponent(
    componentContext = componentContext,
    getFavoriteMediaUseCase = get(),
    handleMediaDoubleClickUseCase = get(),
    componentFactory = get()
)
