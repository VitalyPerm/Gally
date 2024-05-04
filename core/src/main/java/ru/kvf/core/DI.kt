package ru.kvf.core

import org.koin.dsl.module
import ru.kvf.core.data.repository.FavoriteRepositoryImpl
import ru.kvf.core.data.repository.MediaRepositoryImpl
import ru.kvf.core.data.usecase.EdgeToEdgeUseCaseImpl
import ru.kvf.core.data.usecase.GetFolderMediaUseCaseImpl
import ru.kvf.core.data.usecase.GetFoldersUseCaseImpl
import ru.kvf.core.data.usecase.GetMediaUseCaseImpl
import ru.kvf.core.data.usecase.GetSortedMediaUseCaseImpl
import ru.kvf.core.data.usecase.GridCellsCountChangeUseCaseImpl
import ru.kvf.core.data.usecase.LoadMediaUseCaseImpl
import ru.kvf.core.data.usecase.MediaSortByUseCaseImpl
import ru.kvf.core.data.usecase.PerformHapticFeedBackUseCaseImpl
import ru.kvf.core.data.usecase.ThemeUseCaseImpl
import ru.kvf.core.data.usecase.favorite.HandleFavoriteSetUseCaseImpl
import ru.kvf.core.data.usecase.favorite.GetFavoriteFoldersIdsUseCaseImpl
import ru.kvf.core.data.usecase.favorite.GetFavoriteFoldersUseCaseImpl
import ru.kvf.core.data.usecase.favorite.GetFavoriteMediaIdsUseCaseImpl
import ru.kvf.core.data.usecase.favorite.GetFavoriteMediaUseCaseImpl
import ru.kvf.core.data.usecase.favorite.HandleFavoriteClickUseCaseImpl
import ru.kvf.core.data.usecase.favorite.HandleFolderFavoriteClickUseCaseImpl
import ru.kvf.core.domain.repository.FavoriteRepository
import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.core.domain.usecase.EdgeToEdgeUseCase
import ru.kvf.core.domain.usecase.GetFolderMediaUseCase
import ru.kvf.core.domain.usecase.GetFoldersUseCase
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.domain.usecase.GetSortedMediaUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.LoadMediaUseCase
import ru.kvf.core.domain.usecase.MediaSortByUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.domain.usecase.ThemeUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFavoriteSetUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteFoldersIdsUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteFoldersUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaIdsUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFavoriteClickUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFolderFavoriteClickUseCase

val coreModule = module {
    single<MediaRepository> { MediaRepositoryImpl(get()) }
    single<FavoriteRepository> { FavoriteRepositoryImpl(get()) }
    single<GetFavoriteFoldersIdsUseCase> { GetFavoriteFoldersIdsUseCaseImpl(get()) }
    single<GetFavoriteFoldersUseCase> { GetFavoriteFoldersUseCaseImpl(get(), get()) }
    single<GetFavoriteMediaIdsUseCase> { GetFavoriteMediaIdsUseCaseImpl(get()) }
    single<GetFavoriteMediaUseCase> { GetFavoriteMediaUseCaseImpl(get(), get()) }
    single<HandleFavoriteSetUseCase> { HandleFavoriteSetUseCaseImpl(get(), get()) }
    single<HandleFavoriteClickUseCase> { HandleFavoriteClickUseCaseImpl(get(), get()) }
    single<HandleFolderFavoriteClickUseCase> { HandleFolderFavoriteClickUseCaseImpl(get(), get()) }
    single<GetMediaUseCase> { GetMediaUseCaseImpl(get()) }
    single<GetFoldersUseCase> { GetFoldersUseCaseImpl(get()) }
    single<PerformHapticFeedBackUseCase> { PerformHapticFeedBackUseCaseImpl() }
    single<MediaSortByUseCase> { MediaSortByUseCaseImpl(get()) }
    single<LoadMediaUseCase> { LoadMediaUseCaseImpl(get()) }
    single<GridCellsCountChangeUseCase> { GridCellsCountChangeUseCaseImpl(get()) }
    single<GetSortedMediaUseCase> { GetSortedMediaUseCaseImpl(get(), get()) }
    single<GetFolderMediaUseCase> { GetFolderMediaUseCaseImpl(get(), get()) }
    single<EdgeToEdgeUseCase> { EdgeToEdgeUseCaseImpl(get()) }
    single<ThemeUseCase> { ThemeUseCaseImpl(get()) }
}
