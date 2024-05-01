package ru.kvf.favorite

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.kvf.core.ComponentFactory
import ru.kvf.feature.favorite.FavoriteComponent
import ru.kvf.feature.favorite.RealFavoriteComponent
import ru.kvf.feature.favorite.folders.FavoriteFoldersComponent
import ru.kvf.feature.favorite.folders.RealFavoriteFoldersComponent
import ru.kvf.feature.favorite.media.FavoriteMediaComponent
import ru.kvf.feature.favorite.media.RealFavoriteMediaComponent

fun ComponentFactory.createFavoriteComponent(
    componentContext: ComponentContext,
): ru.kvf.feature.favorite.FavoriteComponent =
    ru.kvf.feature.favorite.RealFavoriteComponent(componentContext, get())

fun ComponentFactory.createFavoriteFoldersComponent(
    componentContext: ComponentContext,
): ru.kvf.feature.favorite.folders.FavoriteFoldersComponent =
    ru.kvf.feature.favorite.folders.RealFavoriteFoldersComponent(componentContext)

fun ComponentFactory.createFavoriteMediaComponent(
    componentContext: ComponentContext,
): ru.kvf.feature.favorite.media.FavoriteMediaComponent =
    ru.kvf.feature.favorite.media.RealFavoriteMediaComponent(
        componentContext = componentContext,
        getFavoriteMediaUseCase = get(),
        handleMediaDoubleClickUseCase = get(),
        componentFactory = get()
    )
