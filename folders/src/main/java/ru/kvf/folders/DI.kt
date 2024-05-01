package ru.kvf.folders

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.kvf.core.ComponentFactory
import ru.kvf.feature.folders.FoldersListComponent
import ru.kvf.feature.folders.RealFoldersListComponent

fun ComponentFactory.createFoldersListComponent(
    componentContext: ComponentContext,
    output: (ru.kvf.feature.folders.FoldersListComponent.Output) -> Unit
): ru.kvf.feature.folders.FoldersListComponent = ru.kvf.feature.folders.RealFoldersListComponent(
    componentContext = componentContext,
    onOutput = output,
    getFoldersUseCase = get(),
    gridCellsCountChangeUseCase = get()
)
