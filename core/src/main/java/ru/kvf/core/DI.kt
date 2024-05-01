package ru.kvf.core

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import org.koin.dsl.module
import ru.kvf.core.data.repository.FavoriteRepositoryImpl
import ru.kvf.core.data.repository.MediaRepositoryImpl
import ru.kvf.core.data.usecase.GetFoldersUseCaseImpl
import ru.kvf.core.data.usecase.GetMediaUseCaseImpl
import ru.kvf.core.data.usecase.GridCellsCountChangeUseCaseImpl
import ru.kvf.core.data.usecase.LoadMediaUseCaseImpl
import ru.kvf.core.data.usecase.MediaSortByUseCaseImpl
import ru.kvf.core.data.usecase.PerformHapticFeedBackUseCaseImpl
import ru.kvf.core.data.usecase.favorite.GetFavoriteFoldersIdsUseCaseImpl
import ru.kvf.core.data.usecase.favorite.GetFavoriteFoldersUseCaseImpl
import ru.kvf.core.data.usecase.favorite.GetFavoriteMediaIdsUseCaseImpl
import ru.kvf.core.data.usecase.favorite.GetFavoriteMediaUseCaseImpl
import ru.kvf.core.data.usecase.favorite.HandleFolderDoubleClickUseCaseImpl
import ru.kvf.core.data.usecase.favorite.HandleMediaDoubleClickUseCaseImpl
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.repository.FavoriteRepository
import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.core.domain.usecase.GetFoldersUseCase
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase
import ru.kvf.core.domain.usecase.LoadMediaUseCase
import ru.kvf.core.domain.usecase.MediaSortByUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteFoldersIdsUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteFoldersUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaIdsUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFolderDoubleClickUseCase
import ru.kvf.core.domain.usecase.favorite.HandleMediaDoubleClickUseCase
import ru.kvf.core.mediabsh.MediaBSHComponent
import ru.kvf.core.mediabsh.RealMediaBSHComponent

val coreModule = module {
    single<MediaRepository> { MediaRepositoryImpl(get()) }
    single<FavoriteRepository> { FavoriteRepositoryImpl(get()) }
    single<GetFavoriteFoldersIdsUseCase> { GetFavoriteFoldersIdsUseCaseImpl(get()) }
    single<GetFavoriteFoldersUseCase> { GetFavoriteFoldersUseCaseImpl(get(), get()) }
    single<GetFavoriteMediaIdsUseCase> { GetFavoriteMediaIdsUseCaseImpl(get()) }
    single<GetFavoriteMediaUseCase> { GetFavoriteMediaUseCaseImpl(get(), get()) }
    single<HandleMediaDoubleClickUseCase> { HandleMediaDoubleClickUseCaseImpl(get(), get()) }
    single<HandleFolderDoubleClickUseCase> { HandleFolderDoubleClickUseCaseImpl(get(), get()) }
    single<GetMediaUseCase> { GetMediaUseCaseImpl(get()) }
    single<GetFoldersUseCase> { GetFoldersUseCaseImpl(get()) }
    single<PerformHapticFeedBackUseCase> { PerformHapticFeedBackUseCaseImpl() }
    single<MediaSortByUseCase> { MediaSortByUseCaseImpl(get()) }
    single<LoadMediaUseCase> { LoadMediaUseCaseImpl(get()) }
    single<GridCellsCountChangeUseCase> { GridCellsCountChangeUseCaseImpl(get()) }
}

fun ComponentFactory.createMediaBSHComponent(
    componentContext: ComponentContext,
    media: StateFlow<List<Media>>,
): MediaBSHComponent = RealMediaBSHComponent(
    componentContext = componentContext,
    media = media
)
