package ru.kvf.photos

import org.koin.androidx.compose.get
import org.koin.androidx.compose.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.scope.get
import org.koin.dsl.module
import ru.kvf.photos.details.PhotoDetailsViewModel
import ru.kvf.photos.list.PhotosListViewModel

val photosModule = module {
    viewModelOf(::PhotosListViewModel)
    viewModelOf(::PhotoDetailsViewModel)
    viewModel { params -> PhotoDetailsViewModel(getAllPhotosUseCase = get(), photoId = params.get()) }
}
