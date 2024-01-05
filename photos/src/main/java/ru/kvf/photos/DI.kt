package ru.kvf.photos

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.photos.data.GetFolderPhotosUseCaseImpl
import ru.kvf.photos.data.LoadPhotosUseCaseImpl
import ru.kvf.photos.domain.GetFolderPhotosUseCase
import ru.kvf.photos.domain.LoadPhotosUseCase
import ru.kvf.photos.ui.details.PhotoDetailsViewModel
import ru.kvf.photos.ui.folderdetails.FolderDetailsViewModel
import ru.kvf.photos.ui.list.PhotosListViewModel

val photosModule = module {
    viewModelOf(::PhotosListViewModel)
    viewModelOf(::PhotoDetailsViewModel)
    viewModel { params -> PhotoDetailsViewModel(get(), params.get()) }
    viewModel { params -> FolderDetailsViewModel(params.get(), get(), get(), get()) }
    single<LoadPhotosUseCase> { LoadPhotosUseCaseImpl(get()) }
    single<GetFolderPhotosUseCase> { GetFolderPhotosUseCaseImpl(get()) }
}
