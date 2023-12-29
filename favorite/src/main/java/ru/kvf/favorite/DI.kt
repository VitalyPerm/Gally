package ru.kvf.favorite

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.favorite.data.GetLikedPhotosUseCaseImpl
import ru.kvf.favorite.domain.GetLikedPhotosUseCase
import ru.kvf.favorite.ui.details.FavoriteDetailsViewModel
import ru.kvf.favorite.ui.list.FavoriteListViewModel

val favoriteModule = module {
    viewModelOf(::FavoriteListViewModel)
    viewModel { params -> FavoriteDetailsViewModel(params.get(), get()) }
    single<GetLikedPhotosUseCase> { GetLikedPhotosUseCaseImpl(get(), get()) }
}
