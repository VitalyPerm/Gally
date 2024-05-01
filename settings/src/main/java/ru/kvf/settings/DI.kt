package ru.kvf.settings

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.kvf.core.ComponentFactory
import ru.kvf.core.data.usecase.EdgeToEdgeUseCaseImpl
import ru.kvf.core.data.usecase.ThemeUseCaseImpl
import ru.kvf.core.domain.usecase.EdgeToEdgeUseCase
import ru.kvf.core.domain.usecase.ThemeUseCase
import ru.kvf.feature.settings.RealSettingsListComponent
import ru.kvf.feature.settings.SettingsListComponent

val settingsModule = module {
    single<EdgeToEdgeUseCase> { EdgeToEdgeUseCaseImpl(get()) }
    single<ThemeUseCase> { ThemeUseCaseImpl(get()) }
}

fun ComponentFactory.createSettingsListComponent(
    componentContext: ComponentContext
): ru.kvf.feature.settings.SettingsListComponent =
    ru.kvf.feature.settings.RealSettingsListComponent(
        componentContext = componentContext,
        themeUseCase = get(),
        edgeUseCase = get(),
        sortByUseCase = get(),
        loadMediaUseCase = get()
    )
