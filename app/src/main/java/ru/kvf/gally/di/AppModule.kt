package ru.kvf.gally.di

import android.content.Context
import android.content.res.Resources
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.core.data.PhotosRepositoryImpl
import ru.kvf.core.domain.PhotosRepository
import ru.kvf.photos.list.ListViewModel

val appModule = module {
    single<PhotosRepository> { PhotosRepositoryImpl(get()) }
    viewModelOf(::ListViewModel)
    single<Resources> { get<Context>().resources }
}
