package ru.kvf.core

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.core.data.LikesRepositoryImpl
import ru.kvf.core.data.PhotosRepositoryImpl
import ru.kvf.core.domain.LikesRepository
import ru.kvf.core.domain.PhotosRepository
import ru.kvf.core.ui.details.PhotoDetailsViewModel

val coreModule = module {
    single<PhotosRepository> { PhotosRepositoryImpl(get()) }
    single<LikesRepository> { LikesRepositoryImpl(get()) }
    viewModelOf(::PhotoDetailsViewModel)
}
