package ru.kvf.folders

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.kvf.core.ComponentFactory
import ru.kvf.folders.ui.folderlist.FoldersListComponent
import ru.kvf.folders.ui.folderlist.RealFoldersListComponent

fun ComponentFactory.createFoldersListComponent(
    componentContext: ComponentContext,
    output: (FoldersListComponent.Output) -> Unit
): FoldersListComponent = RealFoldersListComponent(
    componentContext = componentContext,
    onOutput = output,
    getFoldersUseCase = get(),
    gridCellsCountChangeUseCase = get()
)
