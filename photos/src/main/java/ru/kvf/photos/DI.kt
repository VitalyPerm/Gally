package ru.kvf.photos

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.photos.data.GetSortedPhotosUseCaseImpl
import ru.kvf.photos.domain.GetSortedPhotosUseCase
import ru.kvf.photos.ui.detail.PhotoDetailsViewModel
import ru.kvf.photos.ui.list.PhotosListViewModel

val photosModule = module {
    viewModelOf(::PhotosListViewModel)
    viewModel { params -> PhotoDetailsViewModel(get(), params.get()) }
    single<GetSortedPhotosUseCase> { GetSortedPhotosUseCaseImpl(get(), get()) }
}
