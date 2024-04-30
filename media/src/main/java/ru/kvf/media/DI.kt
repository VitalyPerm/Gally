package ru.kvf.media

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.kvf.core.ComponentFactory
import ru.kvf.media.data.GetFolderMediaUseCaseImpl
import ru.kvf.media.data.GetSortedMediaUseCaseImpl
import ru.kvf.media.domain.GetFolderMediaUseCase
import ru.kvf.media.domain.GetSortedMediaUseCase
import ru.kvf.media.ui.list.MediaListComponent
import ru.kvf.media.ui.list.RealMediaListComponent

val mediaModule = module {
    single<GetSortedMediaUseCase> { GetSortedMediaUseCaseImpl(get(), get()) }
    single<GetFolderMediaUseCase> { GetFolderMediaUseCaseImpl(get(), get()) }
}

fun ComponentFactory.createMediaListComponent(
    componentContext: ComponentContext,
    folderName: String? = null,
): MediaListComponent = RealMediaListComponent(
    componentContext = componentContext,
    folderName = folderName,
    getSortedMediaUseCase = get(),
    getFolderMediaUseCase = get(),
    getLikedIdsListUseCase = get(),
    gridCellsCountChangeUseCase = get(),
    handleLikeClickUseCase = get(),
    componentFactory = get()
)
