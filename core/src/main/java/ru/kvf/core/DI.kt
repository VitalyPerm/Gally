package ru.kvf.core

import org.koin.dsl.module
import ru.kvf.core.data.repository.LikesRepositoryImpl
import ru.kvf.core.data.repository.PhotosRepositoryImpl
import ru.kvf.core.data.usecase.GetAllPhotosUseCaseImpl
import ru.kvf.core.data.usecase.GetLikedIdsListUseCaseImpl
import ru.kvf.core.data.usecase.GetSortedPhotosAndFoldersUseCaseImpl
import ru.kvf.core.data.usecase.HandleLikeClickUseCaseImpl
import ru.kvf.core.data.usecase.PerformHapticFeedBackUseCaseImpl
import ru.kvf.core.domain.repository.LikesRepository
import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.core.domain.usecase.GetAllPhotosUseCase
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.domain.usecase.GetSortedPhotosAndFoldersUseCase
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase

val coreModule = module {
    single<PhotosRepository> { PhotosRepositoryImpl(get()) }
    single<LikesRepository> { LikesRepositoryImpl(get()) }
    single<GetSortedPhotosAndFoldersUseCase> { GetSortedPhotosAndFoldersUseCaseImpl(get()) }
    single<GetLikedIdsListUseCase> { GetLikedIdsListUseCaseImpl(get()) }
    single<HandleLikeClickUseCase> { HandleLikeClickUseCaseImpl(get(), get()) }
    single<GetAllPhotosUseCase> { GetAllPhotosUseCaseImpl(get()) }
    single<PerformHapticFeedBackUseCase> { PerformHapticFeedBackUseCaseImpl() }
}
