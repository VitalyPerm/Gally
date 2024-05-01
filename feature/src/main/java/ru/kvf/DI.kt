package ru.kvf

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.kvf.core.ComponentFactory
import ru.kvf.feature.media.MediaListComponent
import ru.kvf.feature.media.RealMediaListComponent

fun ComponentFactory.createMediaListComponent(
    componentContext: ComponentContext,
    folderName: String? = null,
): MediaListComponent = RealMediaListComponent(
    componentContext = componentContext,
    folderName = folderName,
    getSortedMediaUseCase = get(),
    getFolderMediaUseCase = get(),
    getFavoriteMediaIdsUseCase = get(),
    gridCellsCountChangeUseCase = get(),
    handleMediaDoubleClickUseCase = get(),
    componentFactory = get(),
    context = get()
)
