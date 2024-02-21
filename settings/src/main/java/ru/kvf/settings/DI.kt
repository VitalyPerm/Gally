package ru.kvf.settings

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.kvf.core.ComponentFactory
import ru.kvf.settings.data.EdgeToEdgeUseCaseImpl
import ru.kvf.settings.data.ThemeUseCaseImpl
import ru.kvf.settings.domain.EdgeToEdgeUseCase
import ru.kvf.settings.domain.ThemeUseCase
import ru.kvf.settings.ui.list.RealSettingsListComponent
import ru.kvf.settings.ui.list.SettingsListComponent

val settingsModule = module {
    single<EdgeToEdgeUseCase> { EdgeToEdgeUseCaseImpl(get()) }
    single<ThemeUseCase> { ThemeUseCaseImpl(get()) }
}

fun ComponentFactory.createSettingsListComponent(
    componentContext: ComponentContext
): SettingsListComponent = RealSettingsListComponent(
    componentContext = componentContext,
    themeUseCase = get(),
    edgeUseCase = get(),
    sortByUseCase = get(),
    loadPhotosUseCase = get()
)
