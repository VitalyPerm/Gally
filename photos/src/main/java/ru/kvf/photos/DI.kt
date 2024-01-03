package ru.kvf.photos

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.photos.details.PhotoDetailsViewModel
import ru.kvf.photos.list.PhotosListViewModel

val photosModule = module {
    viewModelOf(::PhotosListViewModel)
    viewModelOf(::PhotoDetailsViewModel)
}
