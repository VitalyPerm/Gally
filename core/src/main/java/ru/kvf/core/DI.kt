package ru.kvf.core

import org.koin.dsl.module
import ru.kvf.core.data.repository.LikesRepositoryImpl
import ru.kvf.core.data.repository.PhotosRepositoryImpl
import ru.kvf.core.data.usecase.GetAllPhotosUseCaseImpl
import ru.kvf.core.data.usecase.GetLikedIdsListUseCaseImpl
import ru.kvf.core.data.usecase.GridCellsCountChangeUseCaseImpl
import ru.kvf.core.data.usecase.HandleLikeClickUseCaseImpl
import ru.kvf.core.data.usecase.LoadPhotosUseCaseImpl
import ru.kvf.core.data.usecase.PerformHapticFeedBackUseCaseImpl
import ru.kvf.core.data.usecase.PhotosSortByUseCaseImpl
import ru.kvf.core.domain.repository.LikesRepository
import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.core.domain.usecase.GetAllPhotosUseCase
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.domain.usecase.LoadPhotosUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.domain.usecase.PhotosSortByUseCase

val coreModule = module {
    single<PhotosRepository> { PhotosRepositoryImpl(get()) }
    single<LikesRepository> { LikesRepositoryImpl(get()) }
    single<GetLikedIdsListUseCase> { GetLikedIdsListUseCaseImpl(get()) }
    single<HandleLikeClickUseCase> { HandleLikeClickUseCaseImpl(get(), get()) }
    single<GetAllPhotosUseCase> { GetAllPhotosUseCaseImpl(get()) }
    single<PerformHapticFeedBackUseCase> { PerformHapticFeedBackUseCaseImpl() }
    single<PhotosSortByUseCase> { PhotosSortByUseCaseImpl(get()) }
    single<LoadPhotosUseCase> { LoadPhotosUseCaseImpl(get()) }
    single<GridCellsCountChangeUseCase> { GridCellsCountChangeUseCaseImpl(get()) }
}
