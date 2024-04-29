package ru.kvf.favorite

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.kvf.core.ComponentFactory
import ru.kvf.favorite.ui.FavoriteComponent
import ru.kvf.favorite.ui.FavoriteListComponentOld
import ru.kvf.favorite.ui.RealFavoriteComponent
import ru.kvf.favorite.ui.RealFavoriteListComponentOld
import ru.kvf.favorite.ui.folders.FavoriteFoldersComponent
import ru.kvf.favorite.ui.folders.RealFavoriteFoldersComponent
import ru.kvf.favorite.ui.media.FavoriteMediaComponent
import ru.kvf.favorite.ui.media.RealFavoriteMediaComponent

fun ComponentFactory.createFavoriteListComponentOkd(
    componentContext: ComponentContext,
    output: (FavoriteListComponentOld.Output) -> Unit,
): FavoriteListComponentOld = RealFavoriteListComponentOld(
    componentContext = componentContext,
    onOutput = output,
    getLikedMediaUseCase = get(),
    handleLikeClickUseCase = get()
)

fun ComponentFactory.createFavoriteComponent(
    componentContext: ComponentContext,
): FavoriteComponent = RealFavoriteComponent(componentContext, get())

fun ComponentFactory.createFavoriteFoldersComponent(
    componentContext: ComponentContext,
): FavoriteFoldersComponent = RealFavoriteFoldersComponent(componentContext)

fun ComponentFactory.createFavoriteMediaComponent(
    componentContext: ComponentContext,
): FavoriteMediaComponent = RealFavoriteMediaComponent(componentContext)
