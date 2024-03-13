package ru.kvf.core

import org.koin.dsl.module
import ru.kvf.core.data.repository.LikesRepositoryImpl
import ru.kvf.core.data.repository.MediaRepositoryImpl
import ru.kvf.core.data.usecase.GetMediaUseCaseImpl
import ru.kvf.core.data.usecase.GetLikedIdsListUseCaseImpl
import ru.kvf.core.data.usecase.GetLikedMediaUseCaseImpl
import ru.kvf.core.data.usecase.GridCellsCountChangeUseCaseImpl
import ru.kvf.core.data.usecase.HandleLikeClickUseCaseImpl
import ru.kvf.core.data.usecase.LoadMediaUseCaseImpl
import ru.kvf.core.data.usecase.PerformHapticFeedBackUseCaseImpl
import ru.kvf.core.data.usecase.MediaSortByUseCaseImpl
import ru.kvf.core.domain.repository.LikesRepository
import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.domain.usecase.GetLikedMediaUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.domain.usecase.LoadMediaUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.domain.usecase.MediaSortByUseCase

val coreModule = module {
    single<MediaRepository> { MediaRepositoryImpl(get()) }
    single<LikesRepository> { LikesRepositoryImpl(get()) }
    single<GetLikedIdsListUseCase> { GetLikedIdsListUseCaseImpl(get()) }
    single<HandleLikeClickUseCase> { HandleLikeClickUseCaseImpl(get(), get()) }
    single<GetMediaUseCase> { GetMediaUseCaseImpl(get()) }
    single<PerformHapticFeedBackUseCase> { PerformHapticFeedBackUseCaseImpl() }
    single<MediaSortByUseCase> { MediaSortByUseCaseImpl(get()) }
    single<LoadMediaUseCase> { LoadMediaUseCaseImpl(get()) }
    single<GridCellsCountChangeUseCase> { GridCellsCountChangeUseCaseImpl(get()) }
    single<GetLikedMediaUseCase> { GetLikedMediaUseCaseImpl(get(), get()) }
}
