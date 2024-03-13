package ru.kvf.favorite

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.kvf.core.ComponentFactory
import ru.kvf.favorite.ui.FavoriteListComponent
import ru.kvf.favorite.ui.RealFavoriteListComponent

fun ComponentFactory.createFavoriteListComponent(
    componentContext: ComponentContext,
    output: (FavoriteListComponent.Output) -> Unit,
): FavoriteListComponent = RealFavoriteListComponent(
    componentContext = componentContext,
    onOutput = output,
    getLikedMediaUseCase = get(),
    handleLikeClickUseCase = get()
)
