package ru.kvf.gally.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.gally.feature.photos.data.PhotosRepositoryImpl
import ru.kvf.gally.feature.photos.domain.PhotosRepository
import ru.kvf.gally.feature.photos.ui.root.PhotosRootViewModel

val appModule = module {
    single<PhotosRepository> { PhotosRepositoryImpl(get()) }
    viewModelOf(::PhotosRootViewModel)
}
