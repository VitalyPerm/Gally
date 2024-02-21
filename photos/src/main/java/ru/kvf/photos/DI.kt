package ru.kvf.photos

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.kvf.core.ComponentFactory
import ru.kvf.photos.data.GetFolderPhotosUseCaseImpl
import ru.kvf.photos.data.GetSortedPhotosUseCaseImpl
import ru.kvf.photos.domain.GetFolderPhotosUseCase
import ru.kvf.photos.domain.GetSortedPhotosUseCase
import ru.kvf.photos.ui.detail.PhotoComponent
import ru.kvf.photos.ui.detail.RealPhotoComponent
import ru.kvf.photos.ui.list.PhotosListComponent
import ru.kvf.photos.ui.list.RealPhotosListComponent

val photosModule = module {
    single<GetSortedPhotosUseCase> { GetSortedPhotosUseCaseImpl(get(), get()) }
    single<GetFolderPhotosUseCase> { GetFolderPhotosUseCaseImpl(get(), get()) }
}

fun ComponentFactory.createPhotosListComponent(
    componentContext: ComponentContext,
    output: (PhotosListComponent.Output) -> Unit,
    folderName: String? = null,
): PhotosListComponent = RealPhotosListComponent(
    componentContext = componentContext,
    onOutput = output,
    folderName = folderName,
    getSortedPhotosUseCase = get(),
    getFolderPhotosUseCase = get(),
    getLikedIdsListUseCase = get(),
    getAllPhotosUseCase = get(),
    gridCellsCountChangeUseCase = get(),
    handleLikeClickUseCase = get()
)

fun ComponentFactory.createPhotoComponent(
    componentContext: ComponentContext,
    startIndex: Int
): PhotoComponent = RealPhotoComponent(
    componentContext = componentContext,
    startIndex = startIndex,
    getAllPhotosUseCase = get()
)
