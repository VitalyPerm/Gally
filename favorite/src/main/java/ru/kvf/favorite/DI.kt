package ru.kvf.favorite

import com.arkivanov.decompose.ComponentContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.get
import org.koin.dsl.module
import ru.kvf.core.ComponentFactory
import ru.kvf.favorite.data.GetLikedPhotosUseCaseImpl
import ru.kvf.favorite.domain.GetLikedPhotosUseCase
import ru.kvf.favorite.ui.details.FavoriteDetailsViewModel
import ru.kvf.favorite.ui.list.FavoriteListComponent
import ru.kvf.favorite.ui.list.RealFavoriteListComponent

val favoriteModule = module {
    viewModel { params -> FavoriteDetailsViewModel(params.get(), get()) }
    single<GetLikedPhotosUseCase> { GetLikedPhotosUseCaseImpl(get(), get()) }
}

fun ComponentFactory.createFavoriteListComponent(
    componentContext: ComponentContext
): FavoriteListComponent = RealFavoriteListComponent(
    componentContext = componentContext,
    getLikedPhotosUseCase = get(),
    handleLikeClickUseCase = get()
)
