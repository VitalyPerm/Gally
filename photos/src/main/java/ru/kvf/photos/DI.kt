package ru.kvf.photos

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.photos.details.DetailsViewModel
import ru.kvf.photos.list.ListViewModel

val photosModule = module {
    viewModelOf(::ListViewModel)
    viewModelOf(::DetailsViewModel)
}