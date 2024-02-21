package ru.kvf.folders

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.kvf.core.ComponentFactory
import ru.kvf.folders.data.GetFoldersUseCaseImpl
import ru.kvf.folders.domain.GetFoldersUseCase
import ru.kvf.folders.ui.folderlist.FoldersListComponent
import ru.kvf.folders.ui.folderlist.RealFoldersListComponent

val foldersModule = module {
    single<GetFoldersUseCase> { GetFoldersUseCaseImpl(get()) }
}

fun ComponentFactory.createFoldersListComponent(
    componentContext: ComponentContext,
    output: (FoldersListComponent.Output) -> Unit
): FoldersListComponent = RealFoldersListComponent(
    componentContext = componentContext,
    onOutput = output,
    getFoldersUseCase = get(),
    gridCellsCountChangeUseCase = get()
)
