package ru.kvf.folders

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.folders.data.GetFolderPhotosUseCaseImpl
import ru.kvf.folders.data.GetFoldersUseCaseImpl
import ru.kvf.folders.domain.GetFolderPhotosUseCase
import ru.kvf.folders.domain.GetFoldersUseCase
import ru.kvf.folders.ui.navigation.details.FolderDetailsViewModel
import ru.kvf.folders.ui.navigation.list.FoldersListViewModel

val foldersModule = module {
    single<GetFoldersUseCase> { GetFoldersUseCaseImpl(get()) }
    single<GetFolderPhotosUseCase> { GetFolderPhotosUseCaseImpl(get()) }
    viewModel { params ->
        FolderDetailsViewModel(params.get(), get(), get(), get())
    }
    viewModelOf(::FoldersListViewModel)
}
