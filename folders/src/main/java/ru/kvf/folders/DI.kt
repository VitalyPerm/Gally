package ru.kvf.folders

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.folders.data.GetFolderPhotosUseCaseImpl
import ru.kvf.folders.data.GetFoldersUseCaseImpl
import ru.kvf.folders.domain.GetFolderPhotosUseCase
import ru.kvf.folders.domain.GetFoldersUseCase
import ru.kvf.folders.ui.navigation.folderlist.FoldersListViewModel
import ru.kvf.folders.ui.navigation.folderphotolist.FolderPhotosListViewModel
import ru.kvf.folders.ui.navigation.photodetail.FolderPhotoDetailsViewModel

val foldersModule = module {
    single<GetFoldersUseCase> { GetFoldersUseCaseImpl(get()) }
    single<GetFolderPhotosUseCase> { GetFolderPhotosUseCaseImpl(get(), get()) }
    viewModel { params ->
        FolderPhotosListViewModel(params.get(), get(), get(), get())
    }
    viewModel { params -> FolderPhotoDetailsViewModel(params.get(), params.get(), get()) }
    viewModelOf(::FoldersListViewModel)
}
